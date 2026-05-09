package com.ricardo.GymPass.interfaces.rest;

import com.ricardo.GymPass.application.dto.AuthResponse;
import com.ricardo.GymPass.application.dto.LoginRequest;
import com.ricardo.GymPass.application.dto.RegisterRequest;
import com.ricardo.GymPass.application.dto.UserResponse;
import com.ricardo.GymPass.application.service.AuthService;
import com.ricardo.GymPass.domain.entity.User;
import com.ricardo.GymPass.domain.exception.AuthException;
import com.ricardo.GymPass.infrastructure.security.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Value("${server.servlet.session.cookie.secure:true}")
    private boolean cookieSecure;

    @Value("${server.servlet.session.cookie.same-site:strict}")
    private String cookieSameSite;

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

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(HttpServletRequest request) {
        String token = extractTokenFromCookies(request);

        if (token == null) {
            throw new AuthException("UNAUTHORIZED", "Not authenticated");
        }

        if (!jwtUtil.validateToken(token)) {
            throw new AuthException("UNAUTHORIZED", "Invalid or expired session");
        }

        String userId = jwtUtil.extractUserId(token);
        String email = jwtUtil.extractEmail(token);

        return ResponseEntity.ok(new UserResponse(null, email));
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(HttpServletResponse response) {
        clearJwtCookies(response);
        return ResponseEntity.ok(new AuthResponse("Logout successful", null));
    }

    private void setJwtCookies(String token, HttpServletResponse response) {
        String headerAndPayload = jwtUtil.getHeaderAndPayload(token);
        String signature = jwtUtil.getSignature(token);

        Cookie headerPayloadCookie = new Cookie("jwt_header_payload", headerAndPayload);
        headerPayloadCookie.setHttpOnly(false);
        headerPayloadCookie.setSecure(cookieSecure);
        headerPayloadCookie.setPath("/");
        headerPayloadCookie.setMaxAge(30 * 24 * 60 * 60);
        headerPayloadCookie.setAttribute("SameSite", cookieSameSite);
        response.addCookie(headerPayloadCookie);

        Cookie signatureCookie = new Cookie("jwt_signature", signature);
        signatureCookie.setHttpOnly(true);
        signatureCookie.setSecure(cookieSecure);
        signatureCookie.setPath("/");
        signatureCookie.setMaxAge(30 * 24 * 60 * 60);
        signatureCookie.setAttribute("SameSite", cookieSameSite);
        response.addCookie(signatureCookie);
    }

    private void clearJwtCookies(HttpServletResponse response) {
        Cookie headerPayloadCookie = new Cookie("jwt_header_payload", "");
        headerPayloadCookie.setHttpOnly(false);
        headerPayloadCookie.setSecure(cookieSecure);
        headerPayloadCookie.setPath("/");
        headerPayloadCookie.setMaxAge(0);
        headerPayloadCookie.setAttribute("SameSite", cookieSameSite);
        response.addCookie(headerPayloadCookie);

        Cookie signatureCookie = new Cookie("jwt_signature", "");
        signatureCookie.setHttpOnly(true);
        signatureCookie.setSecure(cookieSecure);
        signatureCookie.setPath("/");
        signatureCookie.setMaxAge(0);
        signatureCookie.setAttribute("SameSite", cookieSameSite);
        response.addCookie(signatureCookie);
    }

    private String extractTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        String headerPayload = null;
        String signature = null;

        for (Cookie cookie : cookies) {
            if ("jwt_header_payload".equals(cookie.getName())) {
                headerPayload = cookie.getValue();
            } else if ("jwt_signature".equals(cookie.getName())) {
                signature = cookie.getValue();
            }
        }

        if (headerPayload != null && signature != null) {
            return headerPayload + "." + signature;
        }

        return null;
    }
}