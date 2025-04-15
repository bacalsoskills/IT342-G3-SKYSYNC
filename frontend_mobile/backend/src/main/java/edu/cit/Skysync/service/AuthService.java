package edu.cit.Skysync.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import edu.cit.Skysync.dto.AuthRequest;
import edu.cit.Skysync.dto.AuthResponse;
import edu.cit.Skysync.entity.UserEntity;
import edu.cit.Skysync.repository.UserRepository;
import edu.cit.Skysync.security.JwtUtil;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse login(AuthRequest request) {
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
    
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
    
        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(user.getEmail(), user.getId(), token);
    }
}