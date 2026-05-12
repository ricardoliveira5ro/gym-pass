package com.ricardo.GymPass.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RateLimitingFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private RateLimitingFilter rateLimitingFilter;

    @BeforeEach
    void setUp() {
        rateLimitingFilter = new RateLimitingFilter();
    }

    @Test
    void doFilterInternal_allowsRequestWhenWithinLimit() throws Exception {
        when(request.getRemoteAddr()).thenReturn("10.0.0.1");
        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

        rateLimitingFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(response, never()).setStatus(429);
    }

    @Test
    void doFilterInternal_blocksRequestWhenRateLimitExceeded() throws Exception {
        when(request.getRemoteAddr()).thenReturn("10.0.0.2");
        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

        for (int i = 0; i < 100; i++) {
            rateLimitingFilter.doFilterInternal(request, response, filterChain);
        }

        verify(response).setStatus(429);
        verify(response).setContentType("application/json");
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void doFilterInternal_usesTokenAsClientIdentifier() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer mytoken123");
        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

        rateLimitingFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void getClientIdentifier_returnsIpWhenNoAuthorizationHeader() {
        when(request.getRemoteAddr()).thenReturn("192.168.1.50");

        String clientId = invokeGetClientIdentifier();

        assertEquals("ip:192.168.1.50", clientId);
    }

    @Test
    void getClientIdentifier_returnsTruncatedTokenWhenAuthorizationHeaderPresent() {
        when(request.getHeader("Authorization")).thenReturn("Bearer verylongtoken");

        String clientId = invokeGetClientIdentifier();

        assertTrue(clientId.startsWith("token:"));
    }

    @Test
    void getClientIdentifier_returnsIpWhenAuthorizationHeaderNotBearer() {
        when(request.getHeader("Authorization")).thenReturn("Basic dXNlcjpwYXNz");
        when(request.getRemoteAddr()).thenReturn("192.168.1.1");

        String clientId = invokeGetClientIdentifier();

        assertEquals("ip:192.168.1.1", clientId);
    }

    @Test
    void createBucket_createsBucketWithCorrectLimit() {
        String client = "ip:127.0.0.1";

        var bucket = invokeCreateBucket(client);

        assertNotNull(bucket);
        io.github.bucket4j.Bucket b = (io.github.bucket4j.Bucket) bucket;
        assertTrue(b.tryConsume(100));
    }

    private String invokeGetClientIdentifier() {
        try {
            var method = RateLimitingFilter.class.getDeclaredMethod("getClientIdentifier", HttpServletRequest.class);
            method.setAccessible(true);
            return (String) method.invoke(rateLimitingFilter, request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object invokeCreateBucket(String clientId) {
        try {
            var method = RateLimitingFilter.class.getDeclaredMethod("createBucket", String.class);
            method.setAccessible(true);
            return method.invoke(rateLimitingFilter, clientId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}