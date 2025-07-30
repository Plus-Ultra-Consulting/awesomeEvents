package fr.leplusultra.awesomeevents.repositories.token;

import fr.leplusultra.awesomeevents.model.token.Token;
import fr.leplusultra.awesomeevents.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ITokenRepository extends JpaRepository<Token, Integer> {
    Optional<Token> findByToken(String token);
    Optional<Token> findByUser(User user);
}
