package com.example.orderplatform.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;
import java.util.UUID;

public class AuthDtos {
    public record RegisterRequest(@Email @NotBlank String email, @NotBlank @Size(min = 8) String password,
                                  @NotBlank String fullName, Set<String> roles) {
    }

    public record LoginRequest(@Email @NotBlank String email, @NotBlank String password) {
    }

    public record AuthResponse(UUID userId, String email, Set<String> roles, String accessToken, String refreshToken) {
    }
}
