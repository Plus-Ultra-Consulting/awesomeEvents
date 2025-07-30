package fr.leplusultra.awesomeevents.util;

import fr.leplusultra.awesomeevents.model.user.User;
import fr.leplusultra.awesomeevents.repositories.user.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class UserValidator implements Validator {
    private final IUserRepository userRepository;

    @Autowired
    public UserValidator(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent() && existingUser.get().getId() != user.getId()) {
            errors.rejectValue("email", "", "User with this email is already in database");
        }
    }
}