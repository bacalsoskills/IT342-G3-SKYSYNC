package com.g3SIA.LoginRegisterSkysync.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users") // Explicit table name to avoid conflicts
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String username;

    @Column
    private String passwordResetToken; // Token for password reset

    @Column(nullable = true)
    private Long tokenExpiryTime; // Field to track token expiry time

    // Default Constructor (Required by JPA)
    public User() {}

    // Parameterized Constructor (Optional for easier object creation)
    public User(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    public void setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    public Long getTokenExpiryTime() {
        return tokenExpiryTime;
    }

    public void setTokenExpiryTime(Long tokenExpiryTime) {
        this.tokenExpiryTime = tokenExpiryTime;
    }
}
