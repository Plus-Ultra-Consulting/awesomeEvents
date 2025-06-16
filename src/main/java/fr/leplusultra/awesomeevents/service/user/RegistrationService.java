package fr.leplusultra.awesomeevents.service.user;

import fr.leplusultra.awesomeevents.model.user.User;
import fr.leplusultra.awesomeevents.repositorie.user.IUserRepository;
import fr.leplusultra.awesomeevents.util.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class RegistrationService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(IUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(UserRole.REGULAR);
        user.setCreatedAt(new Date());
        userRepository.save(user);
    }
}
