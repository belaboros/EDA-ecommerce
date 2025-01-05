package org.msffp.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.msffp.order.model.Order;
import org.msffp.order.model.OrderNotFoundException;
import org.msffp.order.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<Order> findAll() {
        return orderService.findAll();
    }

    @GetMapping("/{id}")
    public Order getById(@PathVariable Long id) {
        return orderService.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    }

    @PostMapping
    public Order create(@RequestBody Order order) {
        return orderService.submitNewOrder(order);
    }

    @PutMapping("/{id}")
    public Order modify(@RequestBody Order order) {
        return orderService.modify(order);
    }

    @PostMapping("/cancel/{id}")
    public Order cancel(@PathVariable Long id) {
        return orderService.cancel(id);
    }
}
