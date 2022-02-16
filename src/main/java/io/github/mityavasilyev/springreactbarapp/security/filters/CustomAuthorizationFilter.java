package io.github.mityavasilyev.springreactbarapp.security.filters;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mityavasilyev.springreactbarapp.exceptions.ExceptionUtils;
import io.github.mityavasilyev.springreactbarapp.security.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static io.github.mityavasilyev.springreactbarapp.security.utils.AuthUtils.ROLES_FIELD;
import static java.util.Arrays.stream;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    /**
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     *
     * @param request
     * @param response
     * @param filterChain
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        if (request.getServletPath().equals("/login") || request.getServletPath().equals("/api/auth/refresh")) {
            log.info("Processing unauthenticated request");
            filterChain.doFilter(request, response);
        } else {
            log.info("Processing authenticated request");
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authorizationHeader != null) {
                try {
                    DecodedJWT decodedJWT = TokenProvider.verifyToken(authorizationHeader);

                    String username = decodedJWT.getSubject();
                    String[] roles = decodedJWT.getClaim(ROLES_FIELD).asArray(String.class);

                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    stream(roles).forEach(
                            role -> authorities.add(new SimpleGrantedAuthority(role))
                    );

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(new UsernamePasswordAuthenticationToken(username, null, authorities));

                    filterChain.doFilter(request, response);

                } catch (Exception exception) {
                    log.error("Error during login: {}", exception.getMessage());

                    response.setStatus(FORBIDDEN.value());
                    Map<String, String> error = new HashMap<>();
                    error.put(ExceptionUtils.JSON_FIELD_ERROR, exception.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
