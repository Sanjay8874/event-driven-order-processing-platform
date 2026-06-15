package com.example.orderplatform.auth;

import com.example.orderplatform.auth.AuthDtos.AuthResponse;
import com.example.orderplatform.auth.AuthDtos.LoginRequest;
import com.example.orderplatform.auth.AuthDtos.RegisterRequest;
import com.example.orderplatform.common.ApiResponse;
import jakarta.validation.Valid;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    ApiResponse<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ApiResponse.ok(MDC.get("correlationId"), authService.register(request));
    }

    @PostMapping("/login")
    ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok(MDC.get("correlationId"), authService.login(request));
    }
}
