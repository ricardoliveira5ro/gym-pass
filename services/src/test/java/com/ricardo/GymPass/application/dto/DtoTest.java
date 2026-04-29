package com.ricardo.GymPass.application.dto;

import com.ricardo.GymPass.application.dto.LoginRequest;
import com.ricardo.GymPass.application.dto.RegisterRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DtoTest {

    @Test
    void loginRequest_setters() {
        LoginRequest request = new LoginRequest();
        request.setExternalId("ext-001");
        request.setPassword("password123");

        assertEquals("ext-001", request.getExternalId());
        assertEquals("password123", request.getPassword());
    }

    @Test
    void registerRequest_setters() {
        RegisterRequest request = new RegisterRequest();
        request.setExternalId("ext-001");
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setName("Test User");

        assertEquals("ext-001", request.getExternalId());
        assertEquals("test@example.com", request.getEmail());
        assertEquals("password123", request.getPassword());
        assertEquals("Test User", request.getName());
    }

    @Test
    void authResponse_getters() {
        AuthResponse response = new AuthResponse("Success", "user-123");

        assertEquals("Success", response.getMessage());
        assertEquals("user-123", response.getUserId());
    }

    @Test
    void syncResult_record() {
        SyncResult result = new SyncResult(1, 2, 3, 4, "2026-04-29");

        assertEquals(1, result.usersCreated());
        assertEquals(2, result.usersUpdated());
        assertEquals(3, result.membershipsCreated());
        assertEquals(4, result.membershipsUpdated());
        assertEquals("2026-04-29", result.syncDate());
    }
}