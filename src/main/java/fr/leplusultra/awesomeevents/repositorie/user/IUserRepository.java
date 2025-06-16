package fr.leplusultra.awesomeevents.repositorie.user;

import fr.leplusultra.awesomeevents.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByFirstName(String name);
    Optional<User> findByEmail(String email);
}
