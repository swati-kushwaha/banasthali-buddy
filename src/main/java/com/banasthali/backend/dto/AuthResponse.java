package com.banasthali.backend.dto;

public class AuthResponse {

    private String token;
    private String userId;
    private String username;
    private String email;
    private String message;

    public AuthResponse() {}

    public AuthResponse(String token, String userId, String username, String email) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.email = email;
    }

    public static AuthResponse success(String token, String userId, String username, String email) {
        return new AuthResponse(token, userId, username, email);
    }

    public static AuthResponse message(String message) {
        AuthResponse response = new AuthResponse();
        response.setMessage(message);
        return response;
    }

    // Getters & Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
