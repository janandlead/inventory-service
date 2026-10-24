package com.javacodeex.inventory.controller;

import com.javacodeex.inventory.dto.InventoryResponse;
import com.javacodeex.inventory.service.InventoryService;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * HTTP controller that exposes inventory availability checks.
 */
@Validated
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    /**
     * Creates an inventory controller using the application service layer.
     *
     * @param inventoryService service used to look up and evaluate inventory
     */
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    /**
     * Checks whether the requested quantity is available for a product.
     *
     * @param productCode unique product code to look up
     * @param quantity requested quantity; must be greater than zero
     * @return product details and an availability flag
     */
    @GetMapping("/{productCode}")
    public InventoryResponse checkStock(
            @PathVariable String productCode,
            @RequestParam @Positive Integer quantity) {
        return inventoryService.checkAvailability(productCode, quantity);
    }
}
