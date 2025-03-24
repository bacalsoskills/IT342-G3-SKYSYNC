package com.g3SIA.LoginRegisterSkysync.Controller;

import com.g3SIA.LoginRegisterSkysync.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    // Constructor-based dependency injection (Best Practice)
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> userDetails) {
        return userService.registerUser(userDetails);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> userDetails) {
        return userService.loginUser(userDetails);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        return userService.forgotPassword(email);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");
        return userService.resetPassword(token, newPassword);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }
}
