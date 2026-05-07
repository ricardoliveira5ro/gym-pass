package com.ricardo.GymPass.interfaces.rest;

import com.ricardo.GymPass.application.dto.AuthResponse;
import com.ricardo.GymPass.application.dto.LoginRequest;
import com.ricardo.GymPass.application.dto.RegisterRequest;
import com.ricardo.GymPass.application.service.AuthService;
import com.ricardo.GymPass.infrastructure.security.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Value("${app.env}")
    private String appEnv;

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request, HttpServletResponse response) {
        AuthService.AuthResult result = authService.register(request);
        setJwtCookies(result.token(), response);
        return ResponseEntity.ok(new AuthResponse("Registration successful", result.userId()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        AuthService.AuthResult result = authService.login(request);
        setJwtCookies(result.token(), response);
        return ResponseEntity.ok(new AuthResponse("Login successful", result.userId()));
    }

    private void setJwtCookies(String token, HttpServletResponse response) {
        String headerAndPayload = jwtUtil.getHeaderAndPayload(token);
        String signature = jwtUtil.getSignature(token);

        Cookie headerPayloadCookie = new Cookie("jwt_header_payload", headerAndPayload);
        headerPayloadCookie.setHttpOnly(false);
        headerPayloadCookie.setSecure(appEnv.equalsIgnoreCase("PROD"));
        headerPayloadCookie.setAttribute("SameSite", appEnv.equalsIgnoreCase("PROD") ? "Strict" : "Lax");
        headerPayloadCookie.setPath("/");
        headerPayloadCookie.setMaxAge(30 * 24 * 60 * 60);
        response.addCookie(headerPayloadCookie);

        Cookie signatureCookie = new Cookie("jwt_signature", signature);
        signatureCookie.setHttpOnly(true);
        signatureCookie.setSecure(appEnv.equalsIgnoreCase("PROD"));
        signatureCookie.setAttribute("SameSite", appEnv.equalsIgnoreCase("PROD") ? "Strict" : "Lax");
        signatureCookie.setPath("/");
        signatureCookie.setMaxAge(30 * 24 * 60 * 60);
        response.addCookie(signatureCookie);
    }
}