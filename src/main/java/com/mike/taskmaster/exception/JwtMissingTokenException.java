package com.mike.taskmaster.exception;

public class JwtMissingTokenException extends RuntimeException{
    
    public JwtMissingTokenException(String message) {
        super(message);
    }
    public JwtMissingTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
