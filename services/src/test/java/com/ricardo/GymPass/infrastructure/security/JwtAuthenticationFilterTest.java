package com.ricardo.GymPass.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtil);
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_setsAuthenticationWhenValidTokenInCookies() throws Exception {
        Cookie headerCookie = new Cookie("jwt_header_payload", "header.payload");
        Cookie signatureCookie = new Cookie("jwt_signature", "signature");
        when(request.getCookies()).thenReturn(new Cookie[]{headerCookie, signatureCookie});
        when(jwtUtil.validateToken("header.payload.signature")).thenReturn(true);
        when(jwtUtil.extractId("header.payload.signature")).thenReturn("123");
        when(jwtUtil.extractEmail("header.payload.signature")).thenReturn("test@example.com");
        when(jwtUtil.extractRole("header.payload.signature")).thenReturn("MEMBER");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertEquals("123", auth.getPrincipal());
        assertEquals("test@example.com", auth.getCredentials());
        assertTrue(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MEMBER")));
    }

    @Test
    void doFilterInternal_setsAuthenticationWhenValidTokenInHeader() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer validtoken");
        when(jwtUtil.validateToken("validtoken")).thenReturn(true);
        when(jwtUtil.extractId("validtoken")).thenReturn("456");
        when(jwtUtil.extractEmail("validtoken")).thenReturn("user@example.com");
        when(jwtUtil.extractRole("validtoken")).thenReturn("ADMIN");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertEquals("456", auth.getPrincipal());
        assertTrue(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void doFilterInternal_doesNotSetAuthenticationWhenNoToken() throws Exception {
        when(request.getCookies()).thenReturn(null);
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNull(auth);
    }

    @Test
    void doFilterInternal_doesNotSetAuthenticationWhenInvalidToken() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalidtoken");
        when(jwtUtil.validateToken("invalidtoken")).thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNull(auth);
    }

    @Test
    void doFilterInternal_doesNotSetAuthenticationWhenTokenInAuthHeaderButNoBearer() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Basic dXNlcjpwYXNz");
        when(request.getCookies()).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNull(auth);
    }

    @Test
    void shouldNotFilter_returnsTrueForLoginEndpoint() {
        when(request.getServletPath()).thenReturn("/api/v1/auth/login");

        boolean result = jwtAuthenticationFilter.shouldNotFilter(request);

        assertTrue(result);
    }

    @Test
    void shouldNotFilter_returnsTrueForRegisterEndpoint() {
        when(request.getServletPath()).thenReturn("/api/v1/auth/register");

        boolean result = jwtAuthenticationFilter.shouldNotFilter(request);

        assertTrue(result);
    }

    @Test
    void shouldNotFilter_returnsFalseForOtherEndpoints() {
        when(request.getServletPath()).thenReturn("/api/v1/members");

        boolean result = jwtAuthenticationFilter.shouldNotFilter(request);

        assertFalse(result);
    }

    @Test
    void extractTokenFromCookies_returnsNullWhenNoCookies() {
        when(request.getCookies()).thenReturn(null);

        String result = invokeExtractTokenFromCookies();

        assertNull(result);
    }

    @Test
    void extractTokenFromCookies_returnsTokenWhenBothCookiesExist() {
        Cookie headerCookie = new Cookie("jwt_header_payload", "header.payload");
        Cookie signatureCookie = new Cookie("jwt_signature", "signature");
        when(request.getCookies()).thenReturn(new Cookie[]{headerCookie, signatureCookie});

        String result = invokeExtractTokenFromCookies();

        assertEquals("header.payload.signature", result);
    }

    @Test
    void extractTokenFromCookies_returnsNullWhenOnlyOneCookieExists() {
        Cookie headerCookie = new Cookie("jwt_header_payload", "header.payload");
        when(request.getCookies()).thenReturn(new Cookie[]{headerCookie});

        String result = invokeExtractTokenFromCookies();

        assertNull(result);
    }

    private String invokeExtractTokenFromCookies() {
        try {
            var method = JwtAuthenticationFilter.class.getDeclaredMethod("extractTokenFromCookies", HttpServletRequest.class);
            method.setAccessible(true);
            return (String) method.invoke(jwtAuthenticationFilter, request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}