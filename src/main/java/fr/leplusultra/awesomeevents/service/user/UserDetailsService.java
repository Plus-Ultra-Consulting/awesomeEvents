package fr.leplusultra.awesomeevents.service.user;

import fr.leplusultra.awesomeevents.model.user.User;
import fr.leplusultra.awesomeevents.repositorie.user.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final IUserRepository userRepository;

    @Autowired
    public UserDetailsService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByEmail(username);
        if (user.isEmpty()) throw new UsernameNotFoundException("User not found!");

        return new fr.leplusultra.awesomeevents.security.UserDetails(user.get());
    }
}
