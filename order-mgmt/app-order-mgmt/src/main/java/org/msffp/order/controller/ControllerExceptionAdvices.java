package org.msffp.order.controller;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.msffp.order.model.OrderNotFoundException;

@RestControllerAdvice
public class ControllerExceptionAdvices {

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String notFoundHandler(OrderNotFoundException ex) {
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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String illegalArgumentHandler(IllegalStateException ex) {
        return "Invalid state. " + ex.getMessage();
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String optimisticLockHandler(ObjectOptimisticLockingFailureException ex) {
        return "Outdated entity version received. " + ex.getMessage();
    }
}
