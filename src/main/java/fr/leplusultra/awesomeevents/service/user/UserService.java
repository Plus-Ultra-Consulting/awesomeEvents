package fr.leplusultra.awesomeevents.service.user;

import fr.leplusultra.awesomeevents.dto.UserDTO;
import fr.leplusultra.awesomeevents.model.user.User;
import fr.leplusultra.awesomeevents.repositories.user.IUserRepository;
import fr.leplusultra.awesomeevents.util.UserRole;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final ModelMapper modelMapper;
    private final IUserRepository userRepository;

    @Autowired
    public UserService(ModelMapper modelMapper, IUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    @Transactional
    public int register(User user) {
        user.setRole(UserRole.REGULAR);
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user).getId();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Transactional
    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void update(User user, User editedUser) {
        user.setFirstName(editedUser.getFirstName());
        user.setLastName(editedUser.getLastName());
        user.setEmail(editedUser.getEmail());

        save(user);
    }

    public User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    public UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}
