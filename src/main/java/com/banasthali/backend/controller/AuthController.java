package com.banasthali.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import com.banasthali.backend.dto.AuthResponse;
import com.banasthali.backend.dto.LoginRequest;
import com.banasthali.backend.dto.RegisterRequest;
import com.banasthali.backend.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "User registration and login endpoints")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new student", description = "Creates a new student account and returns a JWT token")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(AuthResponse.message(e.getMessage()));
        } catch (org.springframework.dao.DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(AuthResponse.message("Database error while creating user"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(AuthResponse.message("Unexpected error during registration"));
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticates a user and returns a JWT token")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException | AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(AuthResponse.message(e.getMessage()));
        }
    }

    // forgot password
    @PostMapping("/forgot-password")
    @Operation(summary = "Forgot password", description = "Sends reset password link to email")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {

        try {

            String email = request.get("email");

            authService.processForgotPassword(email);

            return ResponseEntity.ok(Map.of(
                    "message", "Reset link sent to email"
            ));

        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()
            ));

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Error sending reset email"
            ));

        }
    }



    @PostMapping("/reset-password")
    @Operation(summary = "Reset password", description = "Updates password using reset token")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {

        try {

            String token = request.get("token");
            String password = request.get("password");

            authService.resetPassword(token, password);

            return ResponseEntity.ok(Map.of(
                    "message", "Password updated successfully"
            ));

        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()
            ));

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Error resetting password"
            ));

        }
    }
}
