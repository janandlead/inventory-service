package com.javacodeex.inventory.exception;

/**
 * Signals that an inventory lookup could not find the requested product.
 */
public class ProductNotFoundException extends RuntimeException {

	/**
	 * Creates an exception with a client-facing description of the missing product.
	 *
	 * @param message detail describing the product that was not found
	 */
	public ProductNotFoundException(String message) {
		super(message);
	}
}
