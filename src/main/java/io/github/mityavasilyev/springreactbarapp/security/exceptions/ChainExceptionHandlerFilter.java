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

import static io.github.mityavasilyev.springreactbarapp.exceptions.ExceptionUtils.buildErrorBody;

@Slf4j
@Component
public class ChainExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            log.error("Spring Security Filter Chain Exception: {}", ex.getClass());
            String errorBody = new ObjectMapper().writeValueAsString(buildErrorBody(ex.getMessage()));

            if (ex instanceof SecurityException) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.getWriter().write(errorBody);
                response.getWriter().flush();
//                filterChain.doFilter(request, response);
                return;
            }
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

    }
}
