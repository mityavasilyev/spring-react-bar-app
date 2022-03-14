package io.github.mityavasilyev.springreactbarapp.security.exceptions;

public class InvalidJwtTokenException extends RuntimeException {
    public InvalidJwtTokenException() {
        super();
    }

    public InvalidJwtTokenException(String message) {
        super(message);
    }
}
