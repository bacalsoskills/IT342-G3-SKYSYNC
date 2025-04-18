package edu.cit.Skysync.controller;


import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.cit.Skysync.dto.AuthResponse;
import edu.cit.Skysync.entity.UserEntity;
import edu.cit.Skysync.security.JwtUtil;
import edu.cit.Skysync.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // Get user by ID
    @GetMapping
    public ResponseEntity<UserEntity> getUserById(@RequestParam Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create a new user
    @PostMapping
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity user) {
        UserEntity createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    // Update an existing user
    @PutMapping
    public ResponseEntity<AuthResponse> updateUser(@RequestParam Long id, @RequestBody UserEntity updatedUser) {
        Optional<UserEntity> updated = userService.updateUser(id, updatedUser);
        if (updated.isPresent()) {
            UserEntity user = updated.get();
            // Generate a new token with updated user details
            String newToken = jwtUtil.generateToken(user.getEmail());
            return ResponseEntity.ok(new AuthResponse(user.getEmail(), user.getId(), newToken));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a user
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@RequestParam Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}