package io.github.mityavasilyev.springreactbarapp.exceptions;

import io.github.mityavasilyev.springreactbarapp.extra.Unit;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static io.github.mityavasilyev.springreactbarapp.exceptions.ExceptionUtils.buildErrorBody;

@Slf4j
@RestControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    // Cocktails, Products and related
    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleDataIntegrityViolation(Exception ex, WebRequest request) {
        log.warn("JWT token has expired: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildErrorBody(ex.getMessage()));
    }

    @ExceptionHandler(value = {DataNotFoundException.class})
    public ResponseEntity<Object> handleDataNotFound(Exception ex, WebRequest request) {
        log.warn("Failed to retrieve data: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(buildErrorBody(ex.getMessage()));
    }

    @ExceptionHandler(value = {NotEnoughProductException.class})
    public ResponseEntity<Object> handleNotEnoughProduct(Exception ex, WebRequest request) {
        log.warn("Not enough product: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body(buildErrorBody(ex.getMessage()));
    }

    @ExceptionHandler(value = {UnitMismatchException.class})
    public ResponseEntity<Object> handleUnitMismatch(Exception ex, WebRequest request) {
        log.warn("Unit mismatch in request: {}", ex.getMessage());

        Map<String, String> units = new HashMap<>();
        units.put("units_available", Arrays.toString(Unit.values()));

        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body(buildErrorBody(ex.getMessage(), units));
    }

    @ExceptionHandler(value = {InvalidIdException.class})
    public ResponseEntity<Object> handleInvalidId(Exception ex, WebRequest request) {
        log.warn("Invalid id provided: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildErrorBody(ex.getMessage()));
    }

    // Security
    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDenied(Exception ex, WebRequest request) {
        log.warn("Access denied: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(buildErrorBody(ex.getMessage()));
    }

    // JWT
    @ExceptionHandler(value = {ExpiredJwtException.class, SignatureException.class})
    public ResponseEntity<Object> handleExpiredJWT(Exception ex, WebRequest request) {
        log.warn("A problem with JWT has occurred: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(buildErrorBody(ex.getMessage()));
    }

    // Internal error
    @ExceptionHandler(value
            = {Exception.class})
    public ResponseEntity<Object> handleInternal(RuntimeException ex, WebRequest request) {
        log.error("{}: {}", ex.getClass(), ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildErrorBody("Some internal error has occurred"));
    }
}
