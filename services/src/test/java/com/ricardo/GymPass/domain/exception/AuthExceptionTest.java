package com.ricardo.GymPass.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthExceptionTest {

    @Test
    void constructor_withCodeAndMessage() {
        AuthException exception = new AuthException("INVALID_CREDENTIALS", "Invalid credentials");

        assertEquals("INVALID_CREDENTIALS", exception.getCode());
        assertEquals("Invalid credentials", exception.getMessage());
    }
}