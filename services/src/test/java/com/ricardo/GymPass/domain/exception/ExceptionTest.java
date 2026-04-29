package com.ricardo.GymPass.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionTest {

    @Test
    void externalApiException_constructor() {
        ExternalApiException ex = new ExternalApiException("API error");

        assertEquals("API error", ex.getMessage());
    }

    @Test
    void resourceNotFoundException_constructor() {
        ResourceNotFoundException ex = new ResourceNotFoundException("User", "id");

        assertEquals("User not found with id: id", ex.getMessage());
    }

    @Test
    void validationException_constructor() {
        ValidationException ex = new ValidationException("INVALID_INPUT", "Invalid input");

        assertEquals("Invalid input", ex.getMessage());
        assertEquals("INVALID_INPUT", ex.getField());
    }
}