package com.javacodeex.inventory.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * JPA entity representing the stock held for one product.
 */
@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_code", nullable = false, unique = true)
    private String productCode;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(nullable = false)
    private Integer quantity;

    /**
     * Required by JPA when materializing an entity from the database.
     */
    protected Inventory() {
    }

    /**
     * Creates a new inventory row.
     *
     * @param productCode unique product identifier
     * @param productName display name of the product
     * @param quantity quantity currently in stock
     */
    public Inventory(String productCode, String productName, Integer quantity) {
        this.productCode = productCode;
        this.productName = productName;
        this.quantity = quantity;
    }

    /**
     * Returns the database-generated primary key.
     *
     * @return entity identifier
     */
    public Long getId() { return id; }

    /**
     * Returns the unique product code.
     *
     * @return product code
     */
    public String getProductCode() { return productCode; }

    /**
     * Returns the product display name.
     *
     * @return product name
     */
    public String getProductName() { return productName; }

    /**
     * Returns the current stock quantity.
     *
     * @return quantity in stock
     */
    public Integer getQuantity() { return quantity; }
}
