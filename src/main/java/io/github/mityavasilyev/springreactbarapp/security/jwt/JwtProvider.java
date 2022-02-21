package io.github.mityavasilyev.springreactbarapp.security.jwt;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.Date;

@Slf4j
public class JwtProvider {

    /**
     * Generates new JWT
     *
     * @param auth authentication containing user info
     * @param expireAfterDays Number of days until token is considered expired
     * @param secretKey Key on which token generation will be based
     * @return Generated JWT
     */
    public static String generateToken(Authentication auth, Integer expireAfterDays, SecretKey secretKey) {
        log.info("New pair of tokens is being generated for user [{}]", auth.getName());

        String accessToken = Jwts.builder()
                .setSubject(auth.getName())
                .claim("authorities", auth.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(expireAfterDays)))
                .signWith(secretKey)
                .compact();

        return accessToken;
    }
}
