package io.github.mityavasilyev.springreactbarapp.security.exceptions;

public class InvalidJwtTokenException extends SecurityException {
    public InvalidJwtTokenException() {
        super();
    }

    public InvalidJwtTokenException(String message) {
        super(message);
    }
}
