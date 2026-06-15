package com.example.orderplatform.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class OrderDtos {
    public record CreateOrderRequest(@NotNull UUID customerId, @NotEmpty List<@Valid OrderItemRequest> items) {
    }

    public record OrderItemRequest(@NotNull UUID inventoryId, @Min(1) int quantity,
                                   @NotNull @DecimalMin("0.01") BigDecimal unitPrice) {
    }

    public record UpdateOrderRequest(OrderStatus status) {
    }

    public record OrderResponse(UUID id, UUID customerId, OrderStatus status, BigDecimal totalAmount,
                                Instant createdAt, Instant updatedAt, List<OrderItemResponse> items) {
    }

    public record OrderItemResponse(UUID inventoryId, int quantity, BigDecimal unitPrice) {
    }
}
