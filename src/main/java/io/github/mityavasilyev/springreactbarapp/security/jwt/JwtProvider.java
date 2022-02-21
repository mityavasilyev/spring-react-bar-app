package io.github.mityavasilyev.springreactbarapp.security.jwt;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.Date;

@Slf4j
public class JwtProvider {

    public static AccessRefreshTokens generateTokens(Authentication auth, Integer expireAfterDays, SecretKey secretKey) {
        log.info("New pair of tokens is being generated for user [{}]", auth.getName());

        String accessToken = Jwts.builder()
                .setSubject(auth.getName())
                .claim("authorities", auth.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(expireAfterDays)))
                .signWith(secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(auth.getName())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(expireAfterDays)))
                .signWith(secretKey)
                .compact();

        return new AccessRefreshTokens(accessToken, refreshToken);
    }

    public record AccessRefreshTokens(String accessToken, String refreshToken) {
    }
}
