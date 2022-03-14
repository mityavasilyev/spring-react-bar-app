package io.github.mityavasilyev.springreactbarapp.security.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class FilterChainExceptionHandler extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Spring Security Filter Chain Exception:", e);

            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> responseBody = new HashMap<>();

            if (e instanceof InvalidJwtTokenException) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                responseBody.put("error", e.getMessage());
                response.getWriter().write(mapper.writeValueAsString(responseBody));
                response.getWriter().flush();
                filterChain.doFilter(request, response);
                return;
            }
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

    }
}
