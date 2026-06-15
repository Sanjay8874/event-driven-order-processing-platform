package com.example.orderplatform.audit;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface AuditLogRepository extends JpaRepository<AuditLogEntity, UUID> {
}
