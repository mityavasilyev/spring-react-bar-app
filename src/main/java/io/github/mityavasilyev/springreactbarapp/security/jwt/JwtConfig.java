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
    private String tokenPrefix;
    private Integer accessTokenExpirationAfterHours = 1;
    private Integer refreshTokenExpirationAfterDays = 1;

    public JwtConfig() {
    }

    /**
     * Returns name of header containing auth JSON Web Token
     */
    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }

    public String getRefreshTokenHeader() {
        return "REFRESH_TOKEN";
    }

    /**
     * Returns secret key entity for JWT generation and verification
     */
    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(getSecretKey().getBytes());
    }
}
