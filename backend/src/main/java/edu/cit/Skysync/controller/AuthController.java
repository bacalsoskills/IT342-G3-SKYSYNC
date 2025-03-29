package edu.cit.Skysync.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.cit.Skysync.dto.AuthRequest;
import edu.cit.Skysync.dto.AuthResponse;
import edu.cit.Skysync.entity.UserEntity;
import edu.cit.Skysync.repository.UserRepository;
import edu.cit.Skysync.security.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthController(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        Optional<UserEntity> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isPresent() && passwordEncoder.matches(request.getPassword(), userOpt.get().getPassword())) {
            UserEntity user = userOpt.get();
            String token = jwtUtil.generateToken(user.getEmail());

            AuthResponse response = new AuthResponse(user.getEmail(), user.getId(), token);
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(401).body(null);
    }
}