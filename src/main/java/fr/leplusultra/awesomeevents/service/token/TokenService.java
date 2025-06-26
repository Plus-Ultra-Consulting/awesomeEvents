package fr.leplusultra.awesomeevents.service.token;

import fr.leplusultra.awesomeevents.model.token.Token;
import fr.leplusultra.awesomeevents.model.user.User;
import fr.leplusultra.awesomeevents.repositories.token.ITokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TokenService {
    private final ITokenRepository tokenRepository;

    @Autowired
    public TokenService(ITokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public void saveOrUpdateToken(User user, String tokenStr, LocalDateTime expiresAt) {
        Optional<Token> existingTokenOpt = tokenRepository.findByUser(user);

        if (existingTokenOpt.isPresent()) {
            Token existingToken = existingTokenOpt.get();
            existingToken.setToken(tokenStr);
            existingToken.setExpiresAt(expiresAt);
            tokenRepository.save(existingToken);
        } else {
            Token newToken = Token.builder()
                    .token(tokenStr)
                    .user(user)
                    .expiresAt(expiresAt)
                    .build();
            tokenRepository.save(newToken);
        }
    }
}
