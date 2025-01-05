package org.msffp.order.service;

import java.util.List;
import java.util.random.RandomGenerator;

import org.msffp.order.model.Product;
import org.msffp.order.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Service
@RequiredArgsConstructor
@Log
public class ProductService {


    private final ProductRepository productRepository;
    private static RandomGenerator rg = java.util.random.RandomGenerator.getDefault();


    @Transactional
    public Product upsert(Product changed) {
        int updatedRowCount = productRepository.upsertWithForwardVersion(changed.getId(), changed.getName(), changed.getUnitPrice(), changed.isActive(), changed.getVersion());
        Product persisted = productRepository
            .findById(changed.getId())
            .orElseThrow( () -> new IllegalStateException("Product not found. ID: " + changed.getId()));

        // if product record was not INSERT-ed/UPDATE-ed (due to smaller version attribute received than local)    
        if (updatedRowCount <= 0) {
            throw new OptimisticLockException("Product change ignored.\n"
                + "Received product: " + changed + "\n"
                + "Existing product: " + persisted  
            );
        }
        return persisted;
    }

    public Product getRandomActiveProduct() {
        List<Product> activeProducts = productRepository.findAll().stream().filter(p -> p.isActive()).toList();
        if (activeProducts.isEmpty()) {
            throw new IllegalStateException("There is no any active product.");
        }

        return activeProducts.get(rg.nextInt(activeProducts.size()));
    }
}
