package com.javacodeex.inventory.service;

import com.javacodeex.inventory.dto.InventoryResponse;
import com.javacodeex.inventory.entity.Inventory;
import com.javacodeex.inventory.exception.ProductNotFoundException;
import com.javacodeex.inventory.repository.InventoryRepository;
import org.springframework.stereotype.Service;

/**
 * Business service for inventory availability checks.
 */
@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    /**
     * Creates the service with its persistence dependency.
     *
     * @param inventoryRepository repository used to retrieve inventory rows
     */
    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    /**
     * Evaluates whether a product has enough stock for a requested quantity.
     *
     * @param productCode unique product code to look up
     * @param requestedQuantity quantity requested by the caller; must be positive
     * @return inventory details and whether stock is sufficient
     * @throws IllegalArgumentException if the requested quantity is null or not positive
     * @throws ProductNotFoundException if no inventory row exists for the product code
     */
    public InventoryResponse checkAvailability(String productCode, Integer requestedQuantity) {
        if (requestedQuantity == null || requestedQuantity <= 0) {
            throw new IllegalArgumentException("quantity must be greater than zero");
        }

        Inventory inventory = inventoryRepository.findByProductCode(productCode)
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + productCode));

        return new InventoryResponse(
                inventory.getProductCode(),
                inventory.getProductName(),
                inventory.getQuantity(),
                inventory.getQuantity() >= requestedQuantity
        );
    }
}
