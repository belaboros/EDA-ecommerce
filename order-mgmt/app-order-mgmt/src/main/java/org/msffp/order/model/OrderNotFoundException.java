package org.msffp.order.model;

public class OrderNotFoundException extends IllegalArgumentException {
    public OrderNotFoundException(Long id) {
        super("Order not found. Invalid order ID: \"" + id + "\"");
    }
}
