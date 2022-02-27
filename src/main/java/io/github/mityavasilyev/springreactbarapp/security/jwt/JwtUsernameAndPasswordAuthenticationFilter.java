package io.github.mityavasilyev.springreactbarapp.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mityavasilyev.springreactbarapp.exceptions.InvalidUserException;
import io.github.mityavasilyev.springreactbarapp.security.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
@Slf4j
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        try {
            //Basic authentication
            UsernameAndPasswordAuthenticationRequest authenticationRequest = new ObjectMapper()
                    .readValue(request.getInputStream(), UsernameAndPasswordAuthenticationRequest.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    authenticationRequest.username(),
                    authenticationRequest.password()
            );

            Authentication authenticate = authenticationManager.authenticate(authentication);
            return authenticate;

        } catch (IOException e) {
            log.error("Error during authentication attempt");
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        log.info("New login for user [{}]", authResult.getName());

        // Generating tokens
        JwtProvider.AccessRefreshTokens accessRefreshTokens = JwtProvider.generateTokens(
                authResult,
                secretKey,
                jwtConfig.getAccessTokenExpirationAfterHours(),
                jwtConfig.getRefreshTokenExpirationAfterDays()
                );

        // Updating user's active refresh token
        String refreshToken = null;
        try {
            refreshToken = authService.updateUserRefreshToken(
                    authResult.getName(),
                    accessRefreshTokens.refreshToken());
        } catch (InvalidUserException e) {
            log.error("Failed to save refreshToken for user {}", authResult.getName());
        }

        // Attaching tokens to response
        response.addHeader(
                jwtConfig.getAuthorizationHeader(),
                jwtConfig.getTokenPrefix() + accessRefreshTokens.accessToken());
        response.addHeader(
                jwtConfig.getRefreshTokenHeader(),
                refreshToken);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        log.warn("New failed attempt to login. Caused by: {}", failed.getMessage());
        super.unsuccessfulAuthentication(request, response, failed);
    }

    private record UsernameAndPasswordAuthenticationRequest(String username, String password) {
    }
}
