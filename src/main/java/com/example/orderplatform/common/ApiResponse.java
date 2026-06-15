package com.example.orderplatform.common;

import java.time.Instant;

public record ApiResponse<T>(Instant timestamp, String correlationId, boolean success, T data, ApiError error) {
    public static <T> ApiResponse<T> ok(String correlationId, T data) {
        return new ApiResponse<>(Instant.now(), correlationId, true, data, null);
    }

    public static ApiResponse<Void> fail(String correlationId, String code, String message) {
        return new ApiResponse<>(Instant.now(), correlationId, false, null, new ApiError(code, message));
    }

    public record ApiError(String code, String message) {
    }
}
