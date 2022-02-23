package io.github.mityavasilyev.springreactbarapp.security.jwt;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

@Slf4j
public class JwtProvider {

    /**
     * Generates new JWT
     *
     * @param auth authentication containing user info
     * @param accessExpiresAfterHours Number of hours until access token is considered expired
     * @param refreshExpiresAfterDays Number of days until refresh token is considered expired
     * @param secretKey Key on which token generation will be based
     * @return Generated JWT
     */
    public static AccessRefreshTokens generateTokens(
            Authentication auth,
            SecretKey secretKey,
            Integer accessExpiresAfterHours,
            Integer refreshExpiresAfterDays
            ) {
        log.info("New pair of tokens is being generated for user [{}]", auth.getName());

        // Calculating access token expiration
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR_OF_DAY, accessExpiresAfterHours);

        String accessToken = Jwts.builder()
                .setSubject(auth.getName())
                .claim("authorities", auth.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(cal.getTime())
                .signWith(secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(auth.getName())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(
                        LocalDate
                                .now()
                                .plusDays(refreshExpiresAfterDays)))
                .signWith(secretKey)
                .compact();

        return new AccessRefreshTokens(accessToken, refreshToken);
    }

    public static AccessRefreshTokens generateTokens(
            UserDetails userDetails,
            SecretKey secretKey,
            Integer accessExpiresAfterHours,
            Integer refreshExpiresAfterDays
            ) {
        log.info("New pair of tokens is being generated for user [{}]", userDetails.getUsername());

        // Calculating access token expiration
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR_OF_DAY, accessExpiresAfterHours);

        String accessToken = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("authorities", userDetails.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(cal.getTime())
                .signWith(secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(
                        LocalDate
                                .now()
                                .plusDays(refreshExpiresAfterDays)))
                .signWith(secretKey)
                .compact();

        return new AccessRefreshTokens(accessToken, refreshToken);
    }

    /**
     * Needed to pass around tokens
     */
    public record AccessRefreshTokens(String accessToken, String refreshToken) {
    }
}
