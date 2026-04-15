package com.project.authentication_system.exception;

import com.project.authentication_system.dto.response.ExceptionResponse;

import lombok.extern.slf4j.Slf4j;

import org.jspecify.annotations.NullMarked;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
@NullMarked
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<ExceptionResponse> handleEmailAlreadyExistException(EmailAlreadyExistException ex) {
        log.warn("Registration failed: {}", ex.getMessage());

        ExceptionResponse response = ExceptionResponse.builder()
                .timestamp(LocalDateTime.now())
                .httpStatus(409)
                .error("Conflict")
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.warn("Validation failed: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach((error) -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        ExceptionResponse response = ExceptionResponse.builder()
                .timestamp(LocalDateTime.now())
                .httpStatus(400)
                .error("Validation Failed")
                .message("Invalid request data")
                .fieldErrors(errors)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUserNotFoundException(UserNotFoundException ex) {
        log.warn("User retrieval failed: {}", ex.getMessage());

        ExceptionResponse response = ExceptionResponse.builder()
                .timestamp(LocalDateTime.now())
                .httpStatus(404)
                .error("Unregistered email")
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(Exception ex) {
        log.warn("Authentication failed: {}", ex.getMessage());

        ExceptionResponse response = ExceptionResponse.builder()
                .timestamp(LocalDateTime.now())
                .httpStatus(401)
                .error("Authentication Failed")
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(UserNotLoggedException.class)
    public ResponseEntity<ExceptionResponse> handleUserNotLoggedException(UserNotLoggedException ex) {
        log.warn("Requires authentication: {}", ex.getMessage());

        ExceptionResponse response = ExceptionResponse.builder()
                .timestamp(LocalDateTime.now())
                .httpStatus(401)
                .error("Unauthorized access")
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(InvalidOtpException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidOtpException(InvalidOtpException ex) {
        log.warn("Otp verification failed: {}", ex.getMessage());

        ExceptionResponse response = ExceptionResponse.builder()
                .timestamp(LocalDateTime.now())
                .httpStatus(400)
                .error("Invalid OTP")
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ExceptionResponse> handleTokenExpiredException(TokenExpiredException ex) {
        log.warn("Token expired: {}", ex.getMessage());

        ExceptionResponse response = ExceptionResponse.builder()
                .timestamp(LocalDateTime.now())
                .httpStatus(401)
                .error("Token expired")
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidOperationException(
            InvalidOperationException ex) {
        log.warn("Invalid operation requested: {}", ex.getMessage());

        ExceptionResponse response = ExceptionResponse.builder()
                .httpStatus(403)
                .error("Invalid operation")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(
            Exception ex) {
        log.warn("Request failed: {}", ex.getMessage());

        ExceptionResponse response = ExceptionResponse.builder()
                .httpStatus(500)
                .error("Request failed")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
