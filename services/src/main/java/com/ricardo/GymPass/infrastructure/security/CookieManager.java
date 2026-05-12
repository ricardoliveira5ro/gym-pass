package com.ricardo.GymPass.infrastructure.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieManager {

    private final JwtUtil jwtUtil;

    @Value("${server.servlet.session.cookie.secure:true}")
    private boolean cookieSecure;

    @Value("${server.servlet.session.cookie.same-site:strict}")
    private String cookieSameSite;

    private static final int TOKEN_MAX_AGE_SECONDS = 30 * 24 * 60 * 60;

    public CookieManager(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public void setJwtCookies(String token, HttpServletResponse response) {
        String headerAndPayload = jwtUtil.getHeaderAndPayload(token);
        String signature = jwtUtil.getSignature(token);

        Cookie headerPayloadCookie = new Cookie("jwt_header_payload", headerAndPayload);
        headerPayloadCookie.setHttpOnly(false);
        headerPayloadCookie.setSecure(cookieSecure);
        headerPayloadCookie.setPath("/");
        headerPayloadCookie.setMaxAge(TOKEN_MAX_AGE_SECONDS);
        headerPayloadCookie.setAttribute("SameSite", cookieSameSite);
        response.addCookie(headerPayloadCookie);

        Cookie signatureCookie = new Cookie("jwt_signature", signature);
        signatureCookie.setHttpOnly(true);
        signatureCookie.setSecure(cookieSecure);
        signatureCookie.setPath("/");
        signatureCookie.setMaxAge(TOKEN_MAX_AGE_SECONDS);
        signatureCookie.setAttribute("SameSite", cookieSameSite);
        response.addCookie(signatureCookie);
    }

    public void clearJwtCookies(HttpServletResponse response) {
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

    public String extractTokenFromCookies(HttpServletRequest request) {
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