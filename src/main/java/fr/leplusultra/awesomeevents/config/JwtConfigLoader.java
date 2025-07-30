package fr.leplusultra.awesomeevents.config;

import fr.leplusultra.awesomeevents.security.JwtUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtConfigLoader {
    @Value("${token.expiration.time}")
    public int expirationTime;

    @PostConstruct
    public void init() {
        JwtUtil.expirationTime = expirationTime;
    }
}
