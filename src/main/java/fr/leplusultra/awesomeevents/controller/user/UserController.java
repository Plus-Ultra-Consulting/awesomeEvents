package fr.leplusultra.awesomeevents.controller.user;

import fr.leplusultra.awesomeevents.dto.ErrorResponse;
import fr.leplusultra.awesomeevents.dto.UserDTO;
import fr.leplusultra.awesomeevents.model.otp.OTP;
import fr.leplusultra.awesomeevents.model.user.User;
import fr.leplusultra.awesomeevents.security.JwtUtil;
import fr.leplusultra.awesomeevents.service.email.EmailService;
import fr.leplusultra.awesomeevents.service.otp.OTPService;
import fr.leplusultra.awesomeevents.service.token.TokenService;
import fr.leplusultra.awesomeevents.service.user.UserService;
import fr.leplusultra.awesomeevents.util.Error;
import fr.leplusultra.awesomeevents.util.UserValidator;
import fr.leplusultra.awesomeevents.util.exception.UserException;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService registrationService;
    private final UserService userService;
    private final UserValidator userValidator;
    private final TokenService tokenService;
    private final OTPService otpService;
    private final EmailService emailService;

    @Autowired
    public UserController(UserService registrationService, UserService userService, UserValidator userValidator, TokenService tokenService, OTPService otpService, EmailService emailService) {
        this.registrationService = registrationService;
        this.userService = userService;
        this.userValidator = userValidator;
        this.tokenService = tokenService;
        this.otpService = otpService;
        this.emailService = emailService;
    }

    @PostMapping("/registration")
    public ResponseEntity<Map<String, Object>> register(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        User user = userService.convertToUser(userDTO);
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            Error.returnErrorToClient(bindingResult);
        }

        int createdUserId = registrationService.register(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", createdUserId));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        String email = request.get("username").toLowerCase().trim();
        String otpCode = request.get("otp");

        validateLoginRequest(email, otpCode);

        User user = userService.findByEmail(email);

        if (user == null) {
            throw new UserException("User not found");
        }

        OTP otp = otpService.findOtpByUserId(user.getId());

        if (otp == null || otp.getCode() == null || !otp.getCode().equals(otpCode) ||
                otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new UserException("Invalid one-time code");
        }

        otpService.invalidateCode(user);

        String token = JwtUtil.generateToken(email);

        LocalDateTime expiresAt = LocalDateTime.now().plusDays(JwtUtil.expirationTime);

        tokenService.saveOrUpdateToken(user, token, expiresAt);

        Map<String, Object> response = new HashMap<>();
        response.put("access_token", token);
        response.put("token_type", "Bearer");
        response.put("expires_at", expiresAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sendOtp")
    public ResponseEntity<HttpStatus> sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("username").toLowerCase().trim();
        User user = userService.findByEmail(email);

        if (user == null) {
            throw new UserException("User not found");
        }

        try {
            otpService.createNewOneTimeCode(user);
            emailService.sendEmailWithOTPToUser(user);
        } catch (MessagingException e) {
            throw new UserException("Error while sending email to user");
        }

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping()
    public UserDTO getUser(Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());

        if (user == null) {
            throw new UserException("Authenticated user not found");
        }

        return userService.convertToUserDTO(user);
    }

    @PatchMapping()
    public ResponseEntity<HttpStatus> edit(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);

        User editedUser = userService.convertToUser(userDTO);
        editedUser.setId(user.getId());

        userValidator.validate(editedUser, bindingResult);

        if (bindingResult.hasErrors()) {
            Error.returnErrorToClient(bindingResult);
        }

        userService.update(user, editedUser);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<HttpStatus> delete(Authentication authentication) {
        User user = getAuthenticatedUser(authentication);

        userService.deleteById(user.getId());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private void validateLoginRequest(String email, String optCode) {
        if (email == null || optCode == null || email.isBlank() || optCode.isBlank()) {
            throw new UserException("Invalid login request");
        }
    }

    private User getAuthenticatedUser(Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());

        if (user == null) {
            throw new UserException("Authenticated user not found");
        }

        return user;
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleUserException(UserException userException) {
        ErrorResponse response = new ErrorResponse(userException.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}