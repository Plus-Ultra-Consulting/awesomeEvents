package fr.leplusultra.awesomeevents.controller.user;

import fr.leplusultra.awesomeevents.dto.ErrorResponse;
import fr.leplusultra.awesomeevents.dto.UserDTO;
import fr.leplusultra.awesomeevents.model.user.User;
import fr.leplusultra.awesomeevents.service.user.UserService;
import fr.leplusultra.awesomeevents.util.Error;
import fr.leplusultra.awesomeevents.util.UserValidator;
import fr.leplusultra.awesomeevents.util.exception.UserException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService registrationService;
    private final UserService userService;
    private final UserValidator userValidator;

    @Autowired
    public UserController(UserService registrationService, UserService userService, UserValidator userValidator) {
        this.registrationService = registrationService;
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @PostMapping("registration")
    public ResponseEntity<HttpStatus> register(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {

        User user = userService.convertToUser(userDTO);
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            Error.returnErrorToClient(bindingResult);
        }

        registrationService.register(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping
    public UserDTO getUser() {
        return null; //TODO return authenticated user
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
    public ResponseEntity<HttpStatus> deleteUser() {
        //TODO extract current session user id
        userService.deleteById(0);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(UserException userException) {
        ErrorResponse response = new ErrorResponse(userException.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}