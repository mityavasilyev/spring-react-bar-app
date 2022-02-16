package io.github.mityavasilyev.springreactbarapp.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.github.mityavasilyev.springreactbarapp.security.role.Role;
import io.github.mityavasilyev.springreactbarapp.security.user.AppUser;
import io.github.mityavasilyev.springreactbarapp.security.utils.AccessRefreshTokens;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

import static io.github.mityavasilyev.springreactbarapp.security.utils.AuthUtils.ROLES_FIELD;

@Slf4j
public class TokenProvider {

    public static final String JSON_FIELD_ACCESS_TOKEN = "access_token";
    public static final String JSON_FIELD_REFRESH_TOKEN = "refresh_token";

    // TODO: 16.02.2022 Get secret from someplace else
    private static final String APPLICATION_SECRET = "supaSecretBarAppSecret";
    private static final String TOKEN_PREREQUISITE = "Bearer ";

    /**
     * Needed for encryption standard
     *
     * @return Encryption algorithm of choice with applied secret
     */
    private static Algorithm getEncryptionAlgorithm() {
        return Algorithm.HMAC256(APPLICATION_SECRET.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generates a new pair of tokens for provided User and issuer
     *
     * @param user   User to assign token to
     * @param issuer Issuer to store in token
     * @return record containing access token and refresh token
     */
    public static AccessRefreshTokens generateTokens(User user, String issuer) {
        Algorithm algorithm = getEncryptionAlgorithm();

        // Creating access token
        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + (10 * 60 * 1000)))
                .withIssuer(issuer)
                .withClaim(ROLES_FIELD,
                        user.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .sign(algorithm);

        // Creating refresh token
        String refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + (30 * 60 * 1000)))
                .withIssuer(issuer)
                .sign(algorithm);

        // Returning pair of tokens
        return new AccessRefreshTokens(accessToken, refreshToken);
    }

    /**
     * Generates new access token for provided appUser and issuer
     *
     * @param appUser User to assign token to
     * @param issuer  URI of requesting issuer for token
     * @return refreshed access token
     */
    public static String refreshToken(AppUser appUser, String issuer) {

        Algorithm algorithm = getEncryptionAlgorithm();

        // Creating new access token
        String accessToken = JWT.create()
                .withSubject(appUser.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + (10 * 60 * 1000)))
                .withIssuer(issuer)
                .withClaim(ROLES_FIELD,
                        appUser.getRoles().stream()
                                .map(Role::getName)
                                .collect(Collectors.toList()))
                .sign(algorithm);

        // Returning new token
        return accessToken;
    }

    /**
     * Verifies and decodes provided header that contains Authorization Token
     * Throws RuntimeException if header is null or token is invalid
     *
     * @param authorizationHeader header that contains token that needs to be decoded and verified
     * @return decoded json web token
     */
    public static DecodedJWT verifyToken(String authorizationHeader) {
        if (authorizationHeader != null) {
            if (authorizationHeader.startsWith(TokenProvider.TOKEN_PREREQUISITE)) {
                String token = authorizationHeader.substring(TokenProvider.TOKEN_PREREQUISITE.length());
                Algorithm algorithm = getEncryptionAlgorithm();
                JWTVerifier verifier = JWT.require(algorithm).build();
                return verifier.verify(token);
            } else {
                log.warn("Invalid access token");
                throw new RuntimeException("Access token is invalid");
            }
        } else {
            log.warn("Missing authorization header");
            throw new RuntimeException("Authorization Header is missing");
        }
    }

}

