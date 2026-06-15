package com.example.orderplatform.payment;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payments")
public class PaymentEntity {
    @Id
    private UUID id = UUID.randomUUID();
    @Column(nullable = false)
    private UUID orderId;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;
    @Column(nullable = false)
    private String status;
    private String paymentReference;
    @Column(nullable = false)
    private int attempts;
    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    protected PaymentEntity() {
    }

    public PaymentEntity(UUID orderId, BigDecimal amount) {
        this.orderId = orderId;
        this.amount = amount;
        this.status = "PENDING";
    }

    public void markPaid(String reference) {
        attempts++;
        status = "PAID";
        paymentReference = reference;
    }

    public UUID getOrderId() { return orderId; }
    public String getPaymentReference() { return paymentReference; }
}
