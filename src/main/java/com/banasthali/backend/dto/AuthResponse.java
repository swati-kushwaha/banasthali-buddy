package com.banasthali.backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String token;
    private String userId;
    private String username;
    private String email;
    private String message;

    public static AuthResponse success(String token, String userId, String username, String email) {
        return new AuthResponse(token, userId, username, email, null);
    }

    public static AuthResponse message(String message) {
        return AuthResponse.builder().message(message).build();
    }
}
