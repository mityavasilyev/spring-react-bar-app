package io.github.mityavasilyev.springreactbarapp.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mityavasilyev.springreactbarapp.security.TokenProvider;
import io.github.mityavasilyev.springreactbarapp.security.utils.AccessRefreshTokens;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static io.github.mityavasilyev.springreactbarapp.exceptions.ExceptionUtils.JSON_FIELD_ERROR;
import static io.github.mityavasilyev.springreactbarapp.security.user.AppUser.JSON_FIELD_PASSWORD;
import static io.github.mityavasilyev.springreactbarapp.security.user.AppUser.JSON_FIELD_USERNAME;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response)
            throws AuthenticationException {

        String username = request.getParameter(JSON_FIELD_USERNAME);
        String password = request.getParameter(JSON_FIELD_PASSWORD);

        log.info("New login attempt with username/password [{}/{}]", username, password);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        log.info("Successful login attempt with username [{}]", request.getParameter(JSON_FIELD_USERNAME));

        // Getting user info
        User user = (User) authResult.getPrincipal();
        String issuer = request.getRequestURI().toString();

        // Generating tokens
        AccessRefreshTokens accessRefreshTokens = TokenProvider.generateTokens(user, issuer);

        // Returning generated tokens
        Map<String, String> tokens = new HashMap<>();
        tokens.put(TokenProvider.JSON_FIELD_ACCESS_TOKEN, accessRefreshTokens.accessToken());
        tokens.put(TokenProvider.JSON_FIELD_REFRESH_TOKEN, accessRefreshTokens.refreshToken());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

    // TODO: 12.02.2022 configure login denial
    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        log.info("Failed login attempt with username [{}]", request.getParameter(JSON_FIELD_USERNAME));

        Map<String, String> denial = new HashMap<>();
        denial.put(JSON_FIELD_USERNAME, request.getParameter(JSON_FIELD_USERNAME));
        denial.put(JSON_FIELD_ERROR, "Invalid password");
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        new ObjectMapper().writeValue(response.getOutputStream(), denial);

//        super.unsuccessfulAuthentication(request, response, failed);
    }
}
