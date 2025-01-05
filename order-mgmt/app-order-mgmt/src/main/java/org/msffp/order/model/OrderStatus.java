package org.msffp.order.model;


public enum OrderStatus {
    CREATED,
    FULFILLED,
    CANCELLED;

    public String toString() {
        return getClass().getSimpleName() + "(" + this.ordinal() + ", " + this.name() + ")";
    }
}
