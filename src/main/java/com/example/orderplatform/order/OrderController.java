package com.example.orderplatform.order;

import com.example.orderplatform.common.ApiResponse;
import com.example.orderplatform.order.OrderDtos.*;
import jakarta.validation.Valid;
import org.slf4j.MDC;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    ApiResponse<OrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {
        return ApiResponse.ok(MDC.get("correlationId"), orderService.create(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER','SUPPORT','ADMIN')")
    ApiResponse<OrderResponse> get(@PathVariable UUID id) {
        return ApiResponse.ok(MDC.get("correlationId"), orderService.get(id));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPPORT','ADMIN')")
    ApiResponse<OrderResponse> update(@PathVariable UUID id, @RequestBody UpdateOrderRequest request) {
        return ApiResponse.ok(MDC.get("correlationId"), orderService.update(id, request));
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('CUSTOMER','SUPPORT','ADMIN')")
    ApiResponse<OrderResponse> cancel(@PathVariable UUID id) {
        return ApiResponse.ok(MDC.get("correlationId"), orderService.cancel(id));
    }
}
