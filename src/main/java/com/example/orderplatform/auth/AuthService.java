package com.example.orderplatform.auth;

import com.example.orderplatform.auth.AuthDtos.AuthResponse;
import com.example.orderplatform.auth.AuthDtos.LoginRequest;
import com.example.orderplatform.auth.AuthDtos.RegisterRequest;
import com.example.orderplatform.common.Exceptions.BusinessException;
import com.example.orderplatform.common.Exceptions.UnauthorizedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {
    private final UserRepository users;
    private final RoleRepository roles;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository users, RoleRepository roles, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.users = users;
        this.roles = roles;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (users.existsByEmail(request.email())) {
            throw new BusinessException("Email already registered");
        }
        Set<String> requestedRoles = request.roles() == null || request.roles().isEmpty() ? Set.of("CUSTOMER") : request.roles();
        Set<RoleEntity> userRoles = requestedRoles.stream()
                .map(role -> roles.findByName(role).orElseThrow(() -> new BusinessException("Unknown role: " + role)))
                .collect(Collectors.toSet());
        UserEntity user = users.save(new UserEntity(request.email(), passwordEncoder.encode(request.password()), request.fullName(), userRoles));
        return response(user);
    }

    public AuthResponse login(LoginRequest request) {
        UserEntity user = users.findByEmail(request.email()).orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid credentials");
        }
        return response(user);
    }

    private AuthResponse response(UserEntity user) {
        Set<String> roleNames = user.getRoles().stream().map(RoleEntity::getName).collect(Collectors.toSet());
        return new AuthResponse(user.getId(), user.getEmail(), roleNames, jwtService.accessToken(user), jwtService.refreshToken(user));
    }
}
