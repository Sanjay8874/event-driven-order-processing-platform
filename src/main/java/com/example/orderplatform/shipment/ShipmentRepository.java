package com.example.orderplatform.shipment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface ShipmentRepository extends JpaRepository<ShipmentEntity, UUID> {
}
