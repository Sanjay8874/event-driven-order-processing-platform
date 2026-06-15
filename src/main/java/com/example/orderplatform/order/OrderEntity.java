package com.example.orderplatform.order;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    private UUID id = UUID.randomUUID();
    @Column(nullable = false)
    private UUID customerId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.CREATED;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;
    @Column(nullable = false)
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> items = new ArrayList<>();

    protected OrderEntity() {
    }

    public OrderEntity(UUID customerId) {
        this.customerId = customerId;
    }

    public void addItem(UUID inventoryId, int quantity, BigDecimal unitPrice) {
        OrderItemEntity item = new OrderItemEntity(this, inventoryId, quantity, unitPrice);
        items.add(item);
        totalAmount = totalAmount.add(unitPrice.multiply(BigDecimal.valueOf(quantity)));
        touch();
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
        touch();
    }

    private void touch() {
        updatedAt = Instant.now();
    }

    public UUID getId() { return id; }
    public UUID getCustomerId() { return customerId; }
    public OrderStatus getStatus() { return status; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public List<OrderItemEntity> getItems() { return items; }
}
