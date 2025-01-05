package org.msffp.order.model;

public class ProductParsingException extends Exception {

    public ProductParsingException(String error) {
        this(error, null);
    }

    public ProductParsingException(String error, Throwable cause) {
        super(error, cause);
    }
}
