package com.example.orderplatform.shipment;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "shipments")
public class ShipmentEntity {
    @Id
    private UUID id = UUID.randomUUID();
    @Column(nullable = false)
    private UUID orderId;
    @Column(nullable = false)
    private String trackingNumber;
    @Column(nullable = false)
    private String status = "CREATED";
    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    protected ShipmentEntity() {
    }

    public ShipmentEntity(UUID orderId, String trackingNumber) {
        this.orderId = orderId;
        this.trackingNumber = trackingNumber;
    }

    public String getTrackingNumber() { return trackingNumber; }
}
