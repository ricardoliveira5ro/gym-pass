package com.ricardo.GymPass.interfaces.rest;

import com.ricardo.GymPass.application.dto.ErrorResponse;
import com.ricardo.GymPass.domain.exception.AuthException;
import com.ricardo.GymPass.domain.exception.ExternalApiException;
import com.ricardo.GymPass.domain.exception.ResourceNotFoundException;
import com.ricardo.GymPass.domain.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(AuthException ex, HttpServletRequest request) {
        logger.warn("Authentication error: {} - {}", ex.getCode(), ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                ex.getCode(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex, HttpServletRequest request) {
        logger.warn("Validation error on field '{}': {}", ex.getField(), ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                "VALIDATION_ERROR",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        logger.warn("Resource not found: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                "RESOURCE_NOT_FOUND",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<ErrorResponse> handleExternalApiException(ExternalApiException ex, HttpServletRequest request) {
        logger.error("External API error: {}", ex.getMessage());
        int statusCode = ex.getStatusCode();
        HttpStatus status = statusCode > 0 ? HttpStatus.valueOf(statusCode) : HttpStatus.BAD_GATEWAY;
        ErrorResponse error = new ErrorResponse(
                "EXTERNAL_API_ERROR",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        logger.warn("Validation errors: {}", errors);
        ErrorResponse error = new ErrorResponse(
                "VALIDATION_ERROR",
                "Invalid request data",
                request.getRequestURI(),
                errors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
        logger.error("Unexpected error occurred", ex);
        ErrorResponse error = new ErrorResponse(
                "INTERNAL_ERROR",
                "An unexpected error occurred. Please try again later.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}