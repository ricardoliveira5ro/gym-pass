package com.ricardo.GymPass.application.service;

import com.ricardo.GymPass.application.dto.LoginRequest;
import com.ricardo.GymPass.application.dto.RegisterRequest;
import com.ricardo.GymPass.domain.entity.User;
import com.ricardo.GymPass.domain.exception.AuthException;
import com.ricardo.GymPass.domain.repository.UserRepository;
import com.ricardo.GymPass.infrastructure.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResult register(RegisterRequest request) {
        var existingUserOpt = userRepository.findByExternalId(request.getExternalId());

        User user;

        if (existingUserOpt.isPresent()) {
            user = existingUserOpt.get();

            if (!user.isImported())
                throw new AuthException("MEMBER_REGISTERED", "Member already registered");

            if (userRepository.findByEmail(request.getEmail()).isPresent())
                throw new AuthException("EMAIL_EXISTS", "Email already exists");

            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setImported(false);
            user = userRepository.save(user);

        } else {
            throw new AuthException("INVALID_CREDENTIALS", "Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getId().toString(), user.getEmail(), user.getRole().name());

        return new AuthResult(token, user.getId().toString());
    }

    public AuthResult login(LoginRequest request) {
        User user = userRepository.findByExternalId(request.getExternalId())
                .orElseThrow(() -> new AuthException("INVALID_CREDENTIALS", "Invalid credentials"));

        if (user.isImported())
            throw new AuthException("INVALID_CREDENTIALS", "Invalid credentials");

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash()))
            throw new AuthException("INVALID_CREDENTIALS", "Invalid credentials");

        String token = jwtUtil.generateToken(user.getId().toString(),  user.getEmail(), user.getRole().name());

        return new AuthResult(token, user.getId().toString());
    }

    public UserResult getUser(String userId) {
        if (userId == null)
            throw new AuthException("UNAUTHORIZED", "Not authenticated");

        var existingUserOpt = userRepository.findByUserId(userId);

        if (existingUserOpt.isEmpty())
            throw new AuthException("UNAUTHORIZED", "Not authenticated");

        User user = existingUserOpt.get();

        return new UserResult(user.getName(), user.getExternalId(), user.getEmail());
    }

    public record AuthResult(String token, String userId) {}
    public record UserResult(String name, String externalId, String email) {}
}