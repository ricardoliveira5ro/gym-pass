package com.ricardo.GymPass.interfaces.rest;

import com.ricardo.GymPass.application.service.AuthService;
import com.ricardo.GymPass.infrastructure.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    @Mock
    private HttpServletResponse httpServletResponse;

    @Test
    void register_returns200() {
        com.ricardo.GymPass.application.dto.RegisterRequest request = new com.ricardo.GymPass.application.dto.RegisterRequest();
        request.setExternalId("ext-001");
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setName("Test User");

        when(authService.register(any())).thenReturn(new AuthService.AuthResult("jwt-token", "1"));
        when(jwtUtil.getHeaderAndPayload(anyString())).thenReturn("header.payload");
        when(jwtUtil.getSignature(anyString())).thenReturn("signature");

        ResponseEntity<?> response = authController.register(request, httpServletResponse);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(authService).register(any());
    }

    @Test
    void login_returns200() {
        com.ricardo.GymPass.application.dto.LoginRequest request = new com.ricardo.GymPass.application.dto.LoginRequest();
        request.setExternalId("ext-001");
        request.setPassword("password123");

        when(authService.login(any())).thenReturn(new AuthService.AuthResult("jwt-token", "1"));
        when(jwtUtil.getHeaderAndPayload(anyString())).thenReturn("header.payload");
        when(jwtUtil.getSignature(anyString())).thenReturn("signature");

        ResponseEntity<?> response = authController.login(request, httpServletResponse);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(authService).login(any());
    }
}