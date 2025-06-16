package fr.leplusultra.awesomeevents.service.user;

import fr.leplusultra.awesomeevents.model.user.User;
import fr.leplusultra.awesomeevents.repositorie.user.IUserRepository;
import fr.leplusultra.awesomeevents.security.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserDataService implements UserDetailsService {
    private final IUserRepository userRepository;

    @Autowired
    public UserDataService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByEmail(username);
        if (user.isEmpty()) throw new UsernameNotFoundException("User not found!");

        return new UserData(user.get());
    }
}