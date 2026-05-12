package com.ricardo.GymPass.interfaces.rest;

import com.ricardo.GymPass.application.dto.AuthResult;
import com.ricardo.GymPass.application.dto.UserResponse;
import com.ricardo.GymPass.application.service.AuthService;
import com.ricardo.GymPass.domain.exception.AuthException;
import com.ricardo.GymPass.infrastructure.security.CookieManager;
import com.ricardo.GymPass.infrastructure.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CookieManager cookieManager;

    @Mock
    private HttpServletResponse httpServletResponse;

    private AuthController authController;

    @BeforeEach
    void setUp() {
        authController = new AuthController(authService, jwtUtil, cookieManager);
    }

    @Test
    void register_returns200() {
        com.ricardo.GymPass.application.dto.RegisterRequest request = new com.ricardo.GymPass.application.dto.RegisterRequest();
        request.setExternalId("ext-001");
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setName("Test User");

        when(authService.register(any())).thenReturn(new AuthResult("jwt-token", "1"));

        ResponseEntity<?> response = authController.register(request, httpServletResponse);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(authService).register(any());
        verify(cookieManager).setJwtCookies(anyString(), eq(httpServletResponse));
    }

    @Test
    void login_returns200() {
        com.ricardo.GymPass.application.dto.LoginRequest request = new com.ricardo.GymPass.application.dto.LoginRequest();
        request.setExternalId("ext-001");
        request.setPassword("password123");

        when(authService.login(any())).thenReturn(new AuthResult("jwt-token", "1"));

        ResponseEntity<?> response = authController.login(request, httpServletResponse);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(authService).login(any());
        verify(cookieManager).setJwtCookies(anyString(), eq(httpServletResponse));
    }

    @Test
    void logout_clearsCookiesAndReturns200() {
        ResponseEntity<?> response = authController.logout(httpServletResponse);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(cookieManager).clearJwtCookies(httpServletResponse);
    }

    @Test
    void me_returnsUserWhenAuthenticated() {
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);

        when(cookieManager.extractTokenFromCookies(httpServletRequest)).thenReturn("token");
        when(jwtUtil.validateToken("token")).thenReturn(true);
        when(jwtUtil.extractId("token")).thenReturn("user-id");
        when(authService.getUser("user-id")).thenReturn(new UserResponse("John Doe", "ext-001", "john@example.com"));

        ResponseEntity<UserResponse> response = authController.me(httpServletRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John Doe", response.getBody().getName());
        assertEquals("john@example.com", response.getBody().getEmail());
    }

    @Test
    void me_throwsExceptionWhenNoToken() {
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(cookieManager.extractTokenFromCookies(httpServletRequest)).thenReturn(null);

        AuthException exception = assertThrows(AuthException.class, () -> authController.me(httpServletRequest));

        assertEquals("UNAUTHORIZED", exception.getCode());
    }

    @Test
    void me_throwsExceptionWhenInvalidToken() {
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(cookieManager.extractTokenFromCookies(httpServletRequest)).thenReturn("invalid-token");
        when(jwtUtil.validateToken("invalid-token")).thenReturn(false);

        AuthException exception = assertThrows(AuthException.class, () -> authController.me(httpServletRequest));

        assertEquals("UNAUTHORIZED", exception.getCode());
    }
}