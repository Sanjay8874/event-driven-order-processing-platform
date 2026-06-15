package com.example.orderplatform.inventory;

import com.example.orderplatform.common.Exceptions.BusinessException;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "inventory")
public class InventoryItem {
    @Id
    private UUID id = UUID.randomUUID();
    @Column(nullable = false, unique = true)
    private String sku;
    @Column(nullable = false)
    private String productName;
    @Column(nullable = false)
    private int availableQuantity;
    @Column(nullable = false)
    private int reservedQuantity;
    private Instant updatedAt = Instant.now();

    protected InventoryItem() {
    }

    public InventoryItem(String sku, String productName, int availableQuantity) {
        this.sku = sku;
        this.productName = productName;
        this.availableQuantity = availableQuantity;
    }

    public void reserve(int quantity) {
        if (availableQuantity < quantity) {
            throw new BusinessException("Insufficient stock for SKU " + sku);
        }
        availableQuantity -= quantity;
        reservedQuantity += quantity;
        updatedAt = Instant.now();
    }

    public void release(int quantity) {
        reservedQuantity = Math.max(0, reservedQuantity - quantity);
        availableQuantity += quantity;
        updatedAt = Instant.now();
    }

    public void updateStock(int quantity) {
        availableQuantity = quantity;
        updatedAt = Instant.now();
    }

    public UUID getId() { return id; }
    public String getSku() { return sku; }
    public String getProductName() { return productName; }
    public int getAvailableQuantity() { return availableQuantity; }
    public int getReservedQuantity() { return reservedQuantity; }
}
