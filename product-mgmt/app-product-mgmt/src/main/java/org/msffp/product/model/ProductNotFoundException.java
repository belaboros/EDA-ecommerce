package org.msffp.product.model;

public class ProductNotFoundException extends IllegalArgumentException {
    public ProductNotFoundException(Long id) {
        super("Order not found. Invalid order ID: \"" + id + "\"");
    }
}
