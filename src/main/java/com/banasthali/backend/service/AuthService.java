package com.banasthali.backend.service;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.banasthali.backend.dto.AuthResponse;
import com.banasthali.backend.dto.LoginRequest;
import com.banasthali.backend.dto.RegisterRequest;
import com.banasthali.backend.model.User;
import com.banasthali.backend.repository.UserRepository;
import com.banasthali.backend.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    // 🔹 SIGNUP
    public AuthResponse register(RegisterRequest request) {

        // Check for duplicate email
        if (userRepository.existsByEmail(request.getEmail())) {
            return AuthResponse.message("Email already registered");
        }

        // Default role
        String role = request.getRole() != null ? request.getRole() : "PASSENGER";

        // Build user
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        try {
            // Save user in MongoDB
            User savedUser = userRepository.save(user);

            // Generate JWT token
            String token = jwtTokenProvider.generateToken(
                    savedUser.getEmail(),
                    savedUser.getId(),
                    savedUser.getRole()
            );

            // Return success response
            return AuthResponse.success(token, savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());

        } catch (DuplicateKeyException e) {
            log.warn("Duplicate email during registration: {}", request.getEmail());
            return AuthResponse.message("Email already registered");
        } catch (Exception e) {
            log.error("Database error during signup", e);
            return AuthResponse.message("Database error while creating user");
        }
    }

    // 🔹 LOGIN
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (AuthenticationException e) {
            return AuthResponse.message("Invalid email or password");
        }

        // Fetch user from DB
        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (user == null) {
            return AuthResponse.message("User not found");
        }

        // Generate token
        String token = jwtTokenProvider.generateToken(
                user.getEmail(),
                user.getId(),
                user.getRole()
        );

        return AuthResponse.success(token, user.getId(), user.getUsername(), user.getEmail());
    }
}