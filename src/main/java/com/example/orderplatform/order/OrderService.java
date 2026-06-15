package com.example.orderplatform.order;

import com.example.orderplatform.common.Exceptions.BusinessException;
import com.example.orderplatform.common.Exceptions.ResourceNotFoundException;
import com.example.orderplatform.events.DomainEvents.OrderCreatedEvent;
import com.example.orderplatform.events.DomainEvents.OrderLine;
import com.example.orderplatform.events.EventPublisher;
import com.example.orderplatform.order.OrderDtos.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepository orders;
    private final EventPublisher eventPublisher;

    public OrderService(OrderRepository orders, EventPublisher eventPublisher) {
        this.orders = orders;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public OrderResponse create(CreateOrderRequest request) {
        OrderEntity order = new OrderEntity(request.customerId());
        request.items().forEach(item -> order.addItem(item.inventoryId(), item.quantity(), item.unitPrice()));
        OrderEntity saved = orders.save(order);
        var lines = saved.getItems().stream()
                .map(item -> new OrderLine(item.getInventoryId(), item.getQuantity(), item.getUnitPrice()))
                .toList();
        eventPublisher.publish(new OrderCreatedEvent(UUID.randomUUID(), saved.getId(), lines, saved.getTotalAmount(), Instant.now()));
        return map(saved);
    }

    @Transactional
    public OrderResponse update(UUID id, UpdateOrderRequest request) {
        OrderEntity order = findEntity(id);
        if (request.status() != null) {
            order.setStatus(request.status());
        }
        return map(order);
    }

    @Transactional
    public OrderResponse cancel(UUID id) {
        OrderEntity order = findEntity(id);
        if (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED) {
            throw new BusinessException("Shipped or delivered orders cannot be cancelled");
        }
        order.setStatus(OrderStatus.FAILED);
        return map(order);
    }

    @Transactional(readOnly = true)
    public OrderResponse get(UUID id) {
        return map(findEntity(id));
    }

    @Transactional
    public void mark(UUID id, OrderStatus status) {
        findEntity(id).setStatus(status);
    }

    private OrderEntity findEntity(UUID id) {
        return orders.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));
    }

    private OrderResponse map(OrderEntity order) {
        return new OrderResponse(order.getId(), order.getCustomerId(), order.getStatus(), order.getTotalAmount(),
                order.getCreatedAt(), order.getUpdatedAt(),
                order.getItems().stream()
                        .map(item -> new OrderItemResponse(item.getInventoryId(), item.getQuantity(), item.getUnitPrice()))
                        .toList());
    }
}
