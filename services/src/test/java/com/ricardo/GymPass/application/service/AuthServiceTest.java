package com.ricardo.GymPass.application.service;

import com.ricardo.GymPass.application.dto.LoginRequest;
import com.ricardo.GymPass.application.dto.RegisterRequest;
import com.ricardo.GymPass.domain.entity.User;
import com.ricardo.GymPass.domain.enums.UserRole;
import com.ricardo.GymPass.domain.exception.AuthException;
import com.ricardo.GymPass.domain.repository.UserRepository;
import com.ricardo.GymPass.infrastructure.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userRepository, passwordEncoder, jwtUtil);
    }

    @Test
    void register_newImportedUser_success() {
        RegisterRequest request = new RegisterRequest();
        request.setExternalId("ext-001");
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setName("Test User");

        User importedUser = new User();
        importedUser.setId(1L);
        importedUser.setExternalId("ext-001");
        importedUser.setEmail("old@example.com");
        importedUser.setPasswordHash("");
        importedUser.setName("Old Name");
        importedUser.setRole(UserRole.MEMBER);
        importedUser.setImported(true);

        when(userRepository.findByExternalId("ext-001")).thenReturn(Optional.of(importedUser));
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(importedUser);
        when(jwtUtil.generateToken(anyString(), anyString(), anyString())).thenReturn("jwt-token");

        AuthService.AuthResult result = authService.register(request);

        assertNotNull(result);
        assertEquals("jwt-token", result.token());
        assertEquals("1", result.userId());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_duplicateEmail_throwsException() {
        RegisterRequest request = new RegisterRequest();
        request.setExternalId("ext-001");
        request.setEmail("existing@example.com");
        request.setPassword("password123");
        request.setName("Test User");

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setExternalId("ext-001");
        existingUser.setEmail("ext-001");
        existingUser.setPasswordHash("");
        existingUser.setImported(true);

        when(userRepository.findByExternalId("ext-001")).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail("existing@example.com")).thenReturn(Optional.of(existingUser));

        AuthException exception = assertThrows(AuthException.class, () -> authService.register(request));
        assertEquals("EMAIL_EXISTS", exception.getCode());
    }

    @Test
    void register_notImportedUser_throwsException() {
        RegisterRequest request = new RegisterRequest();
        request.setExternalId("ext-001");
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setName("Test User");

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setExternalId("ext-001");
        existingUser.setImported(false);

        when(userRepository.findByExternalId("ext-001")).thenReturn(Optional.of(existingUser));

        AuthException exception = assertThrows(AuthException.class, () -> authService.register(request));
        assertEquals("MEMBER_REGISTERED", exception.getCode());
    }

    @Test
    void login_success() {
        LoginRequest request = new LoginRequest();
        request.setExternalId("ext-001");
        request.setPassword("password123");

        User user = new User();
        user.setId(1L);
        user.setExternalId("ext-001");
        user.setEmail("test@example.com");
        user.setPasswordHash("encodedPassword");
        user.setRole(UserRole.MEMBER);
        user.setImported(false);

        when(userRepository.findByExternalId("ext-001")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken(anyString(), anyString(), anyString())).thenReturn("jwt-token");

        AuthService.AuthResult result = authService.login(request);

        assertNotNull(result);
        assertEquals("jwt-token", result.token());
        assertEquals("1", result.userId());
    }

    @Test
    void login_invalidCredentials_throwsException() {
        LoginRequest request = new LoginRequest();
        request.setExternalId("ext-001");
        request.setPassword("wrongpassword");

        when(userRepository.findByExternalId("ext-001")).thenReturn(Optional.empty());

        AuthException exception = assertThrows(AuthException.class, () -> authService.login(request));
        assertEquals("INVALID_CREDENTIALS", exception.getCode());
    }

    @Test
    void login_importedUser_throwsException() {
        LoginRequest request = new LoginRequest();
        request.setExternalId("ext-001");
        request.setPassword("password123");

        User user = new User();
        user.setId(1L);
        user.setExternalId("ext-001");
        user.setPasswordHash("encodedPassword");
        user.setImported(true);

        when(userRepository.findByExternalId("ext-001")).thenReturn(Optional.of(user));

        AuthException exception = assertThrows(AuthException.class, () -> authService.login(request));
        assertEquals("INVALID_CREDENTIALS", exception.getCode());
    }

    @Test
    void login_wrongPassword_throwsException() {
        LoginRequest request = new LoginRequest();
        request.setExternalId("ext-001");
        request.setPassword("wrongpassword");

        User user = new User();
        user.setId(1L);
        user.setExternalId("ext-001");
        user.setPasswordHash("encodedPassword");
        user.setImported(false);

        when(userRepository.findByExternalId("ext-001")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", "encodedPassword")).thenReturn(false);

        AuthException exception = assertThrows(AuthException.class, () -> authService.login(request));
        assertEquals("INVALID_CREDENTIALS", exception.getCode());
    }
}