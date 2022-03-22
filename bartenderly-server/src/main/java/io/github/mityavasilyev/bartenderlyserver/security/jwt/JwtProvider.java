package io.github.mityavasilyev.bartenderlyserver.security.jwt;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Collection;
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
            Integer refreshExpiresAfterDays) {

        String username = auth.getName();
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

        return generateTokens(
                username,
                authorities,
                secretKey,
                accessExpiresAfterHours,
                refreshExpiresAfterDays);
    }

    public static AccessRefreshTokens generateTokens(
            UserDetails userDetails,
            SecretKey secretKey,
            Integer accessExpiresAfterHours,
            Integer refreshExpiresAfterDays) {

        String username = userDetails.getUsername();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        return generateTokens(
                username,
                authorities,
                secretKey,
                accessExpiresAfterHours,
                refreshExpiresAfterDays);
    }

    private static AccessRefreshTokens generateTokens(
            String username,
            Collection<? extends GrantedAuthority> authorities,
            SecretKey secretKey,
            Integer accessExpiresAfterHours,
            Integer refreshExpiresAfterDays) {
        log.info("New pair of tokens is being generated for user [{}]", username);

        // Calculating access token expiration
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR_OF_DAY, accessExpiresAfterHours);

        String accessToken = Jwts.builder()
                .setSubject(username)
                .claim("authorities", authorities)
                .setIssuedAt(new Date())
                .setExpiration(cal.getTime())
                .signWith(secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(username)
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
