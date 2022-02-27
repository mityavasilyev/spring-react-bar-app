package io.github.mityavasilyev.springreactbarapp.security.jwt;


import com.google.common.net.HttpHeaders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "application.jwt")
public class JwtConfig {

    private String secretKey;
    private String tokenPrefix = "Bearer ";
    private String refreshTokenHeader = "REFRESH_TOKEN";
    private Integer accessTokenExpirationAfterHours = 1;
    private Integer refreshTokenExpirationAfterDays = 1;

    public JwtConfig() {
    }

    /**
     * Returns name of header containing auth JSON Web Token
     */
    public static String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }

    /**
     * Returns secret key entity for JWT generation and verification
     */
    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(getSecretKey().getBytes());
    }
}
