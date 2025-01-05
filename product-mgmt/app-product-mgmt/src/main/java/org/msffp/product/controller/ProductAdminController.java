package org.msffp.product.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.msffp.product.model.Product;
import org.msffp.product.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RestController
@RequestMapping("/api/product-admin")
@RequiredArgsConstructor
@Log
public class ProductAdminController {

    private final ProductService productService;
    String NL = System.getProperty("line.separator");

    @PostMapping("/random")
    public Product createRandom() {
        Product persisted = productService.generateRandom();
        log.info(NL+NL+"Generated random product: " + NL + persisted);
        return persisted;
    }

    @PostMapping("/initialize")
    public List<Product> initialize() {
        if (!productService.findAll().isEmpty()) {
            throw new IllegalStateException("The product service has already been initialized");
        }
        List<Product> persisted = productService.generateRandom(30);

        // log the generated products
        ObjectMapper mapper = new ObjectMapper();
        // support Java 8 date time apis
        mapper.registerModule(new JavaTimeModule());
        // StdDateFormat is ISO8601 since jackson 2.9
        mapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
        log.info(
            persisted.stream()
                .limit(5)
                //.map(o -> o.toString())
                //.collect(Collectors.joining(NL+"    ", NL+ "Saving generated random orders:"+NL+"    ", ""))
                
                .map(o -> new JSONObject(o).toString(4))
                .collect(Collectors.joining(NL, NL+ "Generated random orders:"+NL, ""))
        );

        return persisted;
    }

    @PostMapping("/inventory")
    public List<Product> inventory() {
        return productService.broadcastInventory();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
