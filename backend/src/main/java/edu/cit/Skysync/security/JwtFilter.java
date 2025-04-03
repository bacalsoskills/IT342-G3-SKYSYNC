package edu.cit.Skysync.security;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import edu.cit.Skysync.entity.UserEntity;
import edu.cit.Skysync.repository.UserRepository;
import edu.cit.Skysync.service.TokenBlacklistService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final TokenBlacklistService tokenBlacklistService;

    public JwtFilter(JwtUtil jwtUtil, UserRepository userRepository, TokenBlacklistService tokenBlacklistService) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        try {
            // Check if the token is blacklisted
            if (tokenBlacklistService.isTokenBlacklisted(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token is invalid. Please log in again.");
                return;
            }

            String email = jwtUtil.extractEmail(token);
            Optional<UserEntity> userOpt = userRepository.findByEmail(email);

            if (userOpt.isPresent() && jwtUtil.validateToken(token)) {
                UserEntity user = userOpt.get();
                if (!email.equals(user.getEmail())) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Token is invalid due to email mismatch. Please log in again.");
                    return;
                }

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(user.getEmail(), null, Collections.emptyList());

                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid token. Please log in again.");
                return;
            }
        } catch (ExpiredJwtException | SignatureException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token has expired or is invalid. Please log in again.");
            return;
        }

        chain.doFilter(request, response);
    }
}