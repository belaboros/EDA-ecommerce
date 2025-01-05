package org.msffp.product.controller;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.msffp.product.model.ProductNotFoundException;

@RestControllerAdvice
public class ControllerExceptionAdvices {

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String notFoundHandler(ProductNotFoundException ex) {
        return "Order not found. " + ex.getMessage();
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String notFoundHandler(NoSuchElementException ex) {
        return "Order not found. " + ex.getMessage();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String illegalArgumentHandler(IllegalArgumentException ex) {
        return "Bad request. " + ex.getMessage();
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    String illegalStateHandler(IllegalStateException ex) {
        return "Precondition failed. " + ex.getMessage();
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String optimisticLockHandler(ObjectOptimisticLockingFailureException ex) {
        return "Outdated entity version received. " + ex.getMessage();
    }
}
