package com.ricardo.GymPass.infrastructure.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CookieManagerTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private HttpServletResponse httpServletResponse;

    private CookieManager cookieManager;

    @BeforeEach
    void setUp() {
        cookieManager = new CookieManager(jwtUtil);
        ReflectionTestUtils.setField(cookieManager, "cookieSecure", true);
        ReflectionTestUtils.setField(cookieManager, "cookieSameSite", "Strict");
    }

    @Test
    void setJwtCookies_splitsTokenAndSetsCookies() {
        String token = "header.payload.signature";

        when(jwtUtil.getHeaderAndPayload(token)).thenReturn("header.payload");
        when(jwtUtil.getSignature(token)).thenReturn("signature");

        cookieManager.setJwtCookies(token, httpServletResponse);

        verify(httpServletResponse, times(2)).addCookie(any(Cookie.class));
    }

    @Test
    void setJwtCookies_setsCorrectCookieAttributes() {
        String token = "header.payload.signature";
        final Cookie[] capturedCookies = new Cookie[2];
        final int[] index = {0};

        when(jwtUtil.getHeaderAndPayload(token)).thenReturn("header.payload");
        when(jwtUtil.getSignature(token)).thenReturn("signature");
        doAnswer(invocation -> {
            capturedCookies[index[0]++] = invocation.getArgument(0);
            return null;
        }).when(httpServletResponse).addCookie(any(Cookie.class));

        cookieManager.setJwtCookies(token, httpServletResponse);

        assertNotNull(capturedCookies[0]);
        assertEquals("jwt_header_payload", capturedCookies[0].getName());
        assertEquals("header.payload", capturedCookies[0].getValue());
    }

    @Test
    void clearJwtCookies_setsMaxAgeToZero() {
        Cookie[] capturedCookies = new Cookie[2];

        doAnswer(invocation -> {
            capturedCookies[0] = invocation.getArgument(0);
            return null;
        }).when(httpServletResponse).addCookie(any(Cookie.class));

        cookieManager.clearJwtCookies(httpServletResponse);

        assertNotNull(capturedCookies[0]);
        assertEquals(0, capturedCookies[0].getMaxAge());
        assertEquals("", capturedCookies[0].getValue());
    }

    @Test
    void extractTokenFromCookies_returnsNullWhenNoCookies() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(null);

        String result = cookieManager.extractTokenFromCookies(request);

        assertNull(result);
    }

    @Test
    void extractTokenFromCookies_returnsNullWhenPartialCookies() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Cookie cookie = new Cookie("jwt_header_payload", "header.payload");
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});

        String result = cookieManager.extractTokenFromCookies(request);

        assertNull(result);
    }

    @Test
    void extractTokenFromCookies_returnsTokenWhenBothCookiesExist() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Cookie headerCookie = new Cookie("jwt_header_payload", "header.payload");
        Cookie signatureCookie = new Cookie("jwt_signature", "signature");
        when(request.getCookies()).thenReturn(new Cookie[]{headerCookie, signatureCookie});

        String result = cookieManager.extractTokenFromCookies(request);

        assertEquals("header.payload.signature", result);
    }

    @Test
    void extractTokenFromCookies_ignoresOtherCookies() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Cookie otherCookie = new Cookie("other", "value");
        Cookie headerCookie = new Cookie("jwt_header_payload", "header.payload");
        Cookie signatureCookie = new Cookie("jwt_signature", "signature");
        when(request.getCookies()).thenReturn(new Cookie[]{otherCookie, headerCookie, signatureCookie});

        String result = cookieManager.extractTokenFromCookies(request);

        assertEquals("header.payload.signature", result);
    }
}