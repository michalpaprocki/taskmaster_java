package com.mike.taskmaster.advice;

import java.time.LocalDateTime;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mike.taskmaster.exception.JwtValidationException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public record ErrorResponse(String message, String path, String timestamp) {}

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e, HttpServletRequest req) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), req.getRequestURI(), LocalDateTime.now().toString()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException e, HttpServletRequest req) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), req.getRequestURI(), LocalDateTime.now().toString()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleValidation(HttpMessageNotReadableException e, HttpServletRequest req) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), req.getRequestURI(), LocalDateTime.now().toString()));
    }
    
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(DataIntegrityViolationException e, HttpServletRequest req) {
        return ResponseEntity.badRequest().body(new ErrorResponse("Email or name already taken", req.getRequestURI(), LocalDateTime.now().toString()));
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e, HttpServletRequest req) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), req.getRequestURI(), LocalDateTime.now().toString()));
    }
    @ExceptionHandler(JwtValidationException.class)
    public ResponseEntity<ErrorResponse> handleJwtValidationExceptions(JwtValidationException e, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(e.getMessage(), req.getRequestURI(), LocalDateTime.now().toString()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
   public ResponseEntity<ErrorResponse> handleConstraintViolations(ConstraintViolationException e, HttpServletRequest req) {
       return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), req.getRequestURI(), LocalDateTime.now().toString()));
   }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedExceptions(AccessDeniedException e, HttpServletRequest req) {
        return ResponseEntity.status(403).body(new ErrorResponse(e.getMessage(), req.getRequestURI(), LocalDateTime.now().toString()));
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e, HttpServletRequest req) {
        return ResponseEntity.status(401).body(new ErrorResponse(e.getMessage(), req.getRequestURI(), LocalDateTime.now().toString()));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOtherExceptions(Exception e, HttpServletRequest req) {
        return ResponseEntity.internalServerError()
                             .body(new ErrorResponse("Unexpected error: " + e.getMessage(), req.getRequestURI(), LocalDateTime.now().toString()));
    }
}
