package fr.leplusultra.awesomeevents.service.user;

import fr.leplusultra.awesomeevents.model.user.User;
import fr.leplusultra.awesomeevents.repositorie.event.IEventRepository;
import fr.leplusultra.awesomeevents.repositorie.user.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final IEventRepository eventRepository;
    private final IUserRepository userRepository;

    @Autowired
    public UserService(IEventRepository eventRepository, IUserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(int id) {
        return userRepository.findById(id).get();
    }

    @Transactional
    public void createNew(User user) {
        user.setCreatedAt(new Date());
        userRepository.save(user);
    }
}
