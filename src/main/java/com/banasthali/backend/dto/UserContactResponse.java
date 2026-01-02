package com.banasthali.backend.dto;

public class UserContactResponse {

    private String id;
    private String username;
    private String email;

    public UserContactResponse() {}

    public UserContactResponse(String id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
