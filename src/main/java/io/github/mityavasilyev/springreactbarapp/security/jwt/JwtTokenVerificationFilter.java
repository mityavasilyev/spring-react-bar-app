package io.github.mityavasilyev.springreactbarapp.security.jwt;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
public class JwtTokenVerificationFilter extends OncePerRequestFilter {

    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader(JwtConfig.getAuthorizationHeader());

        if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) {
            filterChain.doFilter(request, response);
            log.warn("Skipping JWT token verification filter");
            return;
        }

        // Trimming header
        String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "");
        try {
            // Parsing JWT
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);

            // Building user from token
            Claims body = claimsJws.getBody();
            String username = body.getSubject();
            Collection<Map<String, String>> authorities = (List<Map<String, String>>) body.get("authorities");

            // Accessing user's authorities
            Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                    .map(entry -> new SimpleGrantedAuthority(entry.get("authority")))
                    .collect(Collectors.toSet());

            Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, simpleGrantedAuthorities);

            // Passing authentication down
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (JwtException exception) {
            log.error("Failed to verify token for request [{}]", request.getRequestURL());
            throw new IllegalStateException("JWT Token failed to pass verification");
        }

        filterChain.doFilter(request, response);
    }
}
