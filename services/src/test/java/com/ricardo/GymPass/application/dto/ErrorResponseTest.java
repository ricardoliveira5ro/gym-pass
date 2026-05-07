package com.ricardo.GymPass.application.dto;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void errorResponse_twoArgConstructor() {
        ErrorResponse response = new ErrorResponse("CODE", "Message");

        assertEquals("CODE", response.getCode());
        assertEquals("Message", response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void errorResponse_threeArgConstructor() {
        ErrorResponse response = new ErrorResponse("CODE", "Message", "/api/test");

        assertEquals("CODE", response.getCode());
        assertEquals("Message", response.getMessage());
        assertEquals("/api/test", response.getPath());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void errorResponse_fourArgConstructor() {
        Map<String, String> errors = Map.of("email", "Required");
        ErrorResponse response = new ErrorResponse("CODE", "Message", "/api/test", errors);

        assertEquals("CODE", response.getCode());
        assertEquals("Message", response.getMessage());
        assertEquals("/api/test", response.getPath());
        assertEquals(errors, response.getErrors());
    }
}