package edu.cit.Skysync.dto;

public class AuthResponse {
    private String email;
    private Long userId;
    private String token;

    public AuthResponse(String email, Long userId, String token) {
        this.email = email;
        this.userId = userId;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public Long getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }
}