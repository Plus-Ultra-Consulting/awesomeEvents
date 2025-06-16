package fr.leplusultra.awesomeevents.controller.user;

import fr.leplusultra.awesomeevents.model.user.User;
import fr.leplusultra.awesomeevents.service.user.RegistrationService;
import fr.leplusultra.awesomeevents.util.UserValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final RegistrationService registrationService;
    private final UserValidator userValidator;

    @Autowired
    public AuthController(RegistrationService registrationService, UserValidator userValidator) {
        this.registrationService = registrationService;
        this.userValidator = userValidator;
    }

    @GetMapping("/welcome")
    public String welcome(){
        return "auth/welcome";
    }

    @GetMapping("/login")
    public String login(){
        return "auth/login";
    }

    @GetMapping("registration")
    public String registration(@ModelAttribute("user") User user) {
        return "auth/registration";
    }

    @PostMapping("registration")
    public String register(@ModelAttribute("user") @Valid User user, BindingResult bindingResult){
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()){
            return "auth/registration";
        }
        registrationService.register(user);
        return "auth/welcome";
    }
}
