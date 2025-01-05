package org.msffp.order.controller;

import java.util.List;

import org.msffp.order.model.Order;
import org.msffp.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order-admin")
public class OrderAdminController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/random")
    public List<Order> createRandom(@RequestParam(required=false,defaultValue="1") int count) {
        return orderService.generateRandomOrders(count);
    }

    @PostMapping("/fulfill/{id}")
    public Order fulfill(@PathVariable Long id) {
        return orderService.fulfill(id); 
    }

    @PostMapping("/cancel/{id}")
    public Order cancel(@PathVariable Long id) {
        return orderService.cancel(id);
    }    

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
