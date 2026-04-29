package com.ricardo.GymPass.infrastructure.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = com.ricardo.GymPass.GymPassApplication.class, properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=none",
    "spring.flyway.enabled=false"
})
class FullConfigTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void passwordEncoder_beanExists() {
        PasswordEncoder encoder = context.getBean(PasswordEncoder.class);
        assertNotNull(encoder);
    }

    @Test
    void passwordEncoder_encodesSamePasswordDifferently() {
        PasswordEncoder encoder = context.getBean(PasswordEncoder.class);
        String p1 = encoder.encode("testPassword");
        String p2 = encoder.encode("testPassword");
        assertNotEquals(p1, p2);
    }

    @Test
    void passwordEncoder_doesNotMatchNullPassword() {
        PasswordEncoder encoder = context.getBean(PasswordEncoder.class);
        String encoded = encoder.encode("testPassword");
        assertFalse(encoder.matches(null, encoded));
    }

    @Test
    void restTemplate_beanExists() {
        org.springframework.web.client.RestTemplate restTemplate = 
            context.getBean(org.springframework.web.client.RestTemplate.class);
        assertNotNull(restTemplate);
    }

    @Test
    void securityFilterChain_beanExists() {
        org.springframework.security.web.SecurityFilterChain chain = 
            context.getBean(org.springframework.security.web.SecurityFilterChain.class);
        assertNotNull(chain);
    }

    @Test
    void jwtUtil_beanExists() {
        com.ricardo.GymPass.infrastructure.security.JwtUtil jwtUtil = 
            context.getBean(com.ricardo.GymPass.infrastructure.security.JwtUtil.class);
        assertNotNull(jwtUtil);
    }

    @Test
    void userRepository_beanExists() {
        com.ricardo.GymPass.domain.repository.UserRepository repo = 
            context.getBean(com.ricardo.GymPass.domain.repository.UserRepository.class);
        assertNotNull(repo);
    }

    @Test
    void gymRepository_beanExists() {
        com.ricardo.GymPass.domain.repository.GymRepository repo = 
            context.getBean(com.ricardo.GymPass.domain.repository.GymRepository.class);
        assertNotNull(repo);
    }

    @Test
    void membershipRepository_beanExists() {
        com.ricardo.GymPass.domain.repository.MembershipRepository repo = 
            context.getBean(com.ricardo.GymPass.domain.repository.MembershipRepository.class);
        assertNotNull(repo);
    }

    @Test
    void authService_beanExists() {
        com.ricardo.GymPass.application.service.AuthService authService = 
            context.getBean(com.ricardo.GymPass.application.service.AuthService.class);
        assertNotNull(authService);
    }

    @Test
    void syncService_beanExists() {
        com.ricardo.GymPass.application.service.SyncService syncService = 
            context.getBean(com.ricardo.GymPass.application.service.SyncService.class);
        assertNotNull(syncService);
    }

    @Test
    void authController_beanExists() {
        com.ricardo.GymPass.interfaces.rest.AuthController authController = 
            context.getBean(com.ricardo.GymPass.interfaces.rest.AuthController.class);
        assertNotNull(authController);
    }

    @Test
    void syncController_beanExists() {
        com.ricardo.GymPass.interfaces.rest.SyncController syncController = 
            context.getBean(com.ricardo.GymPass.interfaces.rest.SyncController.class);
        assertNotNull(syncController);
    }

    @Test
    void globalExceptionHandler_beanExists() {
        com.ricardo.GymPass.interfaces.rest.GlobalExceptionHandler handler = 
            context.getBean(com.ricardo.GymPass.interfaces.rest.GlobalExceptionHandler.class);
        assertNotNull(handler);
    }
}