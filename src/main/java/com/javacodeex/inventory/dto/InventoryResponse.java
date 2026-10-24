package com.javacodeex.inventory.dto;

/**
 * Read-only response returned by the inventory availability endpoint.
 *
 * @param productCode unique product identifier
 * @param productName display name of the product
 * @param availableQuantity quantity currently stored in inventory
 * @param available whether the available quantity satisfies the request
 */
public record InventoryResponse(
        String productCode,
        String productName,
        Integer availableQuantity,
        boolean available
) {

    /**
     * @return unique product identifier
     */
    @Override
    public String productCode() {
        return productCode;
    }

    /**
     * @return display name of the product
     */
    @Override
    public String productName() {
        return productName;
    }

    /**
     * @return quantity currently stored in inventory
     */
    @Override
    public Integer availableQuantity() {
        return availableQuantity;
    }

    /**
     * @return whether the requested quantity is available
     */
    @Override
    public boolean available() {
        return available;
    }
}
