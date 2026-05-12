package com.ricardo.GymPass.interfaces.rest;

import com.ricardo.GymPass.application.dto.AuthResponse;
import com.ricardo.GymPass.application.dto.AuthResult;
import com.ricardo.GymPass.application.dto.LoginRequest;
import com.ricardo.GymPass.application.dto.RegisterRequest;
import com.ricardo.GymPass.application.dto.UserResponse;
import com.ricardo.GymPass.application.service.AuthService;
import com.ricardo.GymPass.domain.exception.AuthException;
import com.ricardo.GymPass.infrastructure.security.CookieManager;
import com.ricardo.GymPass.infrastructure.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final CookieManager cookieManager;

    public AuthController(AuthService authService, JwtUtil jwtUtil, CookieManager cookieManager) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
        this.cookieManager = cookieManager;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request, HttpServletResponse response) {
        AuthResult result = authService.register(request);
        cookieManager.setJwtCookies(result.token(), response);
        return ResponseEntity.ok(new AuthResponse("Registration successful", result.externalId()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        AuthResult result = authService.login(request);
        cookieManager.setJwtCookies(result.token(), response);
        return ResponseEntity.ok(new AuthResponse("Login successful", result.externalId()));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(HttpServletRequest request) {
        String token = cookieManager.extractTokenFromCookies(request);

        if (token == null) {
            throw new AuthException("UNAUTHORIZED", "Not authenticated");
        }

        if (!jwtUtil.validateToken(token)) {
            throw new AuthException("UNAUTHORIZED", "Invalid or expired session");
        }

        String id = jwtUtil.extractId(token);
        UserResponse userResult = authService.getUser(id);

        return ResponseEntity.ok(userResult);
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(HttpServletResponse response) {
        cookieManager.clearJwtCookies(response);
        return ResponseEntity.ok(new AuthResponse("Logout successful", null));
    }
}