package io.github.mityavasilyev.springreactbarapp.security.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.Date;

public class JwtProvider {


    public static AccessRefreshTokens generateTokens(Authentication auth, Integer expireAfterDays, SecretKey secretKey) {

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
