package fr.leplusultra.awesomeevents.controller.user;

import fr.leplusultra.awesomeevents.dto.ErrorResponse;
import fr.leplusultra.awesomeevents.dto.UserDTO;
import fr.leplusultra.awesomeevents.model.user.User;
import fr.leplusultra.awesomeevents.security.JwtUtil;
import fr.leplusultra.awesomeevents.service.token.TokenService;
import fr.leplusultra.awesomeevents.service.user.UserService;
import fr.leplusultra.awesomeevents.util.Error;
import fr.leplusultra.awesomeevents.util.UserValidator;
import fr.leplusultra.awesomeevents.util.exception.UserException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService registrationService;
    private final UserService userService;
    private final UserValidator userValidator;
    private final TokenService tokenService;

    @Autowired
    public UserController(UserService registrationService, UserService userService, UserValidator userValidator, TokenService tokenService) {
        this.registrationService = registrationService;
        this.userService = userService;
        this.userValidator = userValidator;
        this.tokenService = tokenService;
    }

    @PostMapping("/registration")
    public ResponseEntity<Map<String, Object>> register(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        User user = userService.convertToUser(userDTO);
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            Error.returnErrorToClient(bindingResult);
        }

        int createdEventId = registrationService.register(user);
        Map<String, Object> response = new HashMap<>();
        response.put("id", createdEventId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        String email = request.get("username");
        User user = userService.findByEmail(email);

        if (user == null) {
            throw new UserException("User not found");
        }

        String token = JwtUtil.generateToken(email);
        Date expiresAt = new Date(System.currentTimeMillis() + JwtUtil.EXPIRATION_TIME);

        tokenService.saveOrUpdateToken(user, token, expiresAt);

        Map<String, Object> response = new HashMap<>();
        response.put("access_token", token);
        response.put("token_type", "Bearer");
        response.put("expires_at", expiresAt);
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public UserDTO getUser(Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());

        if (user == null) {
            throw new UserException("Authenticated user not found");
        }

        return userService.convertToUserDTO(user);
    }

    @PostMapping("/edit")
    public ResponseEntity<HttpStatus> editUser(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        //TODO get current user and update it.
        User user = userService.convertToUser(userDTO);

        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            Error.returnErrorToClient(bindingResult);
        }

        registrationService.save(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<HttpStatus> deleteUser(Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());

        if (user == null) {
            throw new UserException("Authenticated user not found");
        }

        userService.deleteById(user.getId());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(UserException userException) {
        ErrorResponse response = new ErrorResponse(userException.getMessage(), new Date());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}