package org.msffp.product.controller;

import java.util.List;

import org.msffp.product.model.Product;
import org.msffp.product.model.ProductNotFoundException;
import org.msffp.product.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;


@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Log
public class ProductController {

    private final ProductService productService;
    String NL = System.getProperty("line.separator");


    @GetMapping
    public List<Product> findAll(@RequestParam(required=false)Boolean active) {
        return productService.findAll(active);
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id) {
        return productService.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    @PostMapping
    public Product create(@RequestBody Product product) {
        if (product.getId() != null) {
            throw new IllegalArgumentException("Invalid order. Expected null ID. Received: " + product.toString());
        }

        return productService.save(product);
    }

    @PutMapping("/{id}")
    public Product modify(@RequestBody Product product) {
        return productService.save(product);
    }

    @PostMapping("/passive/{id}")
    public Product cancel(@PathVariable Long id) {
        return productService.passivate(id);
    }
}
