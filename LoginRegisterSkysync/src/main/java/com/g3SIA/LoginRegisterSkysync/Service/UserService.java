package com.g3SIA.LoginRegisterSkysync.Service;

import com.g3SIA.LoginRegisterSkysync.Entity.User;
import com.g3SIA.LoginRegisterSkysync.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder; // Add BCrypt for password security

    private static final long TOKEN_EXPIRY_DURATION = 15 * 60 * 1000; // 15 minutes in milliseconds

    public ResponseEntity<?> registerUser(Map<String, String> userDetails) {
        String email = userDetails.get("email");
        String username = userDetails.get("username");
        String password = userDetails.get("password");

        if (userRepository.findByEmail(email) != null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Email is already in use"));
        }

        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(encoder.encode(password)); // Hash password before saving
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "Registration successful"));
    }

    public ResponseEntity<?> loginUser(Map<String, String> userDetails) {
        String email = userDetails.get("email");
        String password = userDetails.get("password");

        User user = userRepository.findByEmail(email);
        if (user != null && encoder.matches(password, user.getPassword())) { // Use BCrypt for validation
            return ResponseEntity.ok(Map.of("message", "Login successful"));
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", "Invalid email or password"));
    }

    public ResponseEntity<?> forgotPassword(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Email not found"));
        }

        // Generate token and expiry time
        String token = UUID.randomUUID().toString();
        long expiryTime = System.currentTimeMillis() + TOKEN_EXPIRY_DURATION;

        user.setPasswordResetToken(token);
        user.setTokenExpiryTime(expiryTime);
        userRepository.save(user);

        // Log the token (remove in production)
        System.out.println("Generated token for email " + email + ": " + token);

        // Optionally send an email (not implemented)
        sendPasswordResetEmail(email, token);

        return ResponseEntity.ok(Map.of("message", "Password reset email sent", "token", token));
    }

    public ResponseEntity<?> resetPassword(String token, String newPassword) {
        User user = userRepository.findByPasswordResetToken(token);

        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Invalid or expired token"));
        }

        if (System.currentTimeMillis() > user.getTokenExpiryTime()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Token has expired. Please request a new one."));
        }

        // Update the password and clear the token
        user.setPassword(encoder.encode(newPassword)); // Hash new password
        user.setPasswordResetToken(null);
        user.setTokenExpiryTime(null); // Ensure this does not cause NullPointerException
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "Password reset successful"));
    }

    private void sendPasswordResetEmail(String email, String token) {
        // Use JavaMailSender or third-party email service for real implementation
        System.out.println("Sending password reset token to " + email + ": " + token);
    }
}
