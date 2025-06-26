package fr.leplusultra.awesomeevents.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class JwtUtil {
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    public static int expirationTime;

    public static String generateToken(String email) {
        LocalDateTime dateNow = LocalDateTime.now();
        LocalDateTime expiryDate = dateNow.plusDays(expirationTime);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(Date.from(dateNow.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(expiryDate.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(key)
                .compact();
    }
}
