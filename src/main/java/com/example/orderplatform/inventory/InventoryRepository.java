package com.example.orderplatform.inventory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface InventoryRepository extends JpaRepository<InventoryItem, UUID> {
}
