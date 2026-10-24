package com.javacodeex.inventory.repository;

import com.javacodeex.inventory.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Persistence operations for {@link Inventory} entities.
 */
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    /**
     * Finds the inventory row associated with a product code.
     *
     * @param productCode product code to search for
     * @return matching inventory row, or empty when no row exists
     */
    Optional<Inventory> findByProductCode(String productCode);
}
