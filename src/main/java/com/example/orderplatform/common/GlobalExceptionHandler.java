package com.example.orderplatform.common;

import com.example.orderplatform.common.Exceptions.BusinessException;
import com.example.orderplatform.common.Exceptions.ResourceNotFoundException;
import com.example.orderplatform.common.Exceptions.UnauthorizedException;
import com.example.orderplatform.common.Exceptions.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<ApiResponse<Void>> notFound(ResourceNotFoundException ex) {
        return response(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler({BusinessException.class, ValidationException.class})
    ResponseEntity<ApiResponse<Void>> business(RuntimeException ex) {
        return response(HttpStatus.BAD_REQUEST, "BUSINESS_RULE_FAILED", ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    ResponseEntity<ApiResponse<Void>> unauthorized(UnauthorizedException ex) {
        return response(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<Void>> validation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .orElse("Invalid request payload");
        return response(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", message);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiResponse<Void>> unexpected(Exception ex, HttpServletRequest request) {
        return response(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "Unexpected error processing " + request.getRequestURI());
    }

    private ResponseEntity<ApiResponse<Void>> response(HttpStatus status, String code, String message) {
        return ResponseEntity.status(status).body(ApiResponse.fail(MDC.get("correlationId"), code, message));
    }
}
