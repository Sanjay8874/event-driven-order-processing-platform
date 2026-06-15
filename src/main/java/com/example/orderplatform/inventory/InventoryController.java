package com.example.orderplatform.inventory;

import com.example.orderplatform.common.ApiResponse;
import jakarta.validation.constraints.Min;
import org.slf4j.MDC;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<InventoryItem> updateStock(@PathVariable UUID id, @RequestParam @Min(0) int quantity) {
        return ApiResponse.ok(MDC.get("correlationId"), inventoryService.updateStock(id, quantity));
    }
}
