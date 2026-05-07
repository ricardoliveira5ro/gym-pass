package com.ricardo.GymPass.infrastructure.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() throws Exception {
        jwtUtil = new JwtUtil();
        Field secretField = JwtUtil.class.getDeclaredField("secret");
        secretField.setAccessible(true);
        secretField.set(jwtUtil, "test-secret-key-for-testing-purposes-only-must-be-at-least-256-bits-long");

        Field expirationField = JwtUtil.class.getDeclaredField("expiration");
        expirationField.setAccessible(true);
        expirationField.set(jwtUtil, 3600000L);
    }

    @Test
    void generateToken_success() {
        String token = jwtUtil.generateToken("1", "test@example.com", "MEMBER");

        assertNotNull(token);
        assertTrue(token.contains("."));
    }

    @Test
    void extractUserId_success() {
        String token = jwtUtil.generateToken("1", "test@example.com", "MEMBER");

        String userId = jwtUtil.extractUserId(token);

        assertEquals("1", userId);
    }

    @Test
    void extractEmail_success() {
        String token = jwtUtil.generateToken("1", "test@example.com", "MEMBER");

        String email = jwtUtil.extractEmail(token);

        assertEquals("test@example.com", email);
    }

    @Test
    void extractRole_success() {
        String token = jwtUtil.generateToken("1", "test@example.com", "MEMBER");

        String role = jwtUtil.extractRole(token);

        assertEquals("MEMBER", role);
    }

    @Test
    void validateToken_validToken_returnsTrue() {
        String token = jwtUtil.generateToken("1", "test@example.com", "MEMBER");

        boolean isValid = jwtUtil.validateToken(token);

        assertTrue(isValid);
    }

    @Test
    void validateToken_invalidToken_returnsFalse() {
        boolean isValid = jwtUtil.validateToken("invalid.token");

        assertFalse(isValid);
    }

    @Test
    void isTokenExpired_validToken_returnsFalse() {
        String token = jwtUtil.generateToken("1", "test@example.com", "MEMBER");

        boolean isExpired = jwtUtil.isTokenExpired(token);

        assertFalse(isExpired);
    }

    @Test
    void getHeaderAndPayload_returnsCorrectParts() {
        String token = jwtUtil.generateToken("1", "test@example.com", "MEMBER");

        String headerPayload = jwtUtil.getHeaderAndPayload(token);

        assertNotNull(headerPayload);
        assertTrue(headerPayload.contains("."));
    }

    @Test
    void getSignature_returnsCorrectPart() {
        String token = jwtUtil.generateToken("1", "test@example.com", "MEMBER");

        String signature = jwtUtil.getSignature(token);

        assertNotNull(signature);
        assertTrue(signature.length() > 0);
    }
}