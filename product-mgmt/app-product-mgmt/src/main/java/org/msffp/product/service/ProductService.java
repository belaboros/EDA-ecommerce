package org.msffp.product.service;

import java.util.List;
import java.util.Optional;
import java.util.random.RandomGenerator;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;
import org.msffp.product.domainevent.DomainEvent;
import org.msffp.product.model.Product;
import org.msffp.product.model.ProductNotFoundException;
import org.msffp.product.repository.DomainEventRepository;

import org.msffp.product.repository.ProductRepository;

import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final DomainEventRepository domainEventRepository;

    public List<Product> findAll() {
        return findAll(null);
    }

    /**
     * Find products.
     * @param active: null to return all products. true/false to return the active/deactivated products.
     * @return
     */
    public List<Product> findAll(Boolean active) {
        if (active == null) {
            return productRepository.findAll();
        }

        return productRepository.findAll().stream().filter(p -> active.equals(p.isActive())).toList();
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    public Product passivate(Long id) {
        Product o = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        if (!o.isActive()) {
            return o;
        }

        o.passivate();

        Product productPersisted = productRepository.save(o);
        domainEventRepository.save(DomainEvent.create(productPersisted));
        return productPersisted;
    }

    @Transactional
    public Product save(Product p) {
        // business pre-conditions for product modifications
        if (p.getId() != null) {
            Product original = productRepository.findById(p.getId()).orElse(null);

            if (original!=null) {
                // identical
                if (original.equals(p)) {
                    return original;
                }

                // reject modification based on an outdated version 
                if (p.getVersion() < original.getVersion()) {
                    throw new OptimisticLockException("Modification based on an outdated version.\n"
                        + "Current product version: " + original.getVersion() + "\n"
                        + "Received product version: " + p.getVersion() + "\n"
                        + "Received product: " + p
                    );
                }

                // re-activation is not allowed
                if (!original.isActive() && p.isActive()) {
                    throw new IllegalArgumentException("A passive product cannot be re-activated. Received: " + p);
                }        

                // modifying a passive product is not allowed
                if (!original.isActive()) {
                    throw new IllegalArgumentException("Modification on a passive product is not allowed. Received: " + p);
                }
            }
        }

        Product persisted = productRepository.save(p);
        domainEventRepository.save(DomainEvent.create(persisted));
        return persisted;
    }

    @Transactional
    public List<Product> saveAll(List<Product> orders) {
        List<Product> productPersisted = productRepository.saveAll(orders);
        domainEventRepository.saveAll(productPersisted.stream().map(pp -> DomainEvent.create(pp)).toList());
        return productPersisted;
    }

    @Transactional
    public List<Product> broadcastInventory() {
        List<Product> products = productRepository.findAll();
        domainEventRepository.saveAll(products.stream().map(pp -> DomainEvent.create(pp, "CurrentStatePublished", null)).toList());
        return products;
    }

    @Transactional
    public void deleteById(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new ProductNotFoundException(id);
        }
    }

    @Transactional
    public Product generateRandom() {
        RandomGenerator rg = new java.util.Random(0);
        Product product =
            Product.builder()
                    .name(getPredefinedProductName((int)productRepository.count()))
                    .unitPrice(rg.nextInt(1, 200)/10.0)
                    .build();

        Product persisted = save(product);

        return persisted;
    }

    @Transactional
    public List<Product> generateRandom(int count) {
        RandomGenerator rg = new java.util.Random(0);
        List<Product> products =
        IntStream
            .range(0, count)
            .mapToObj(
                i -> Product.builder()
                    .name(getPredefinedProductName(i))
                    .unitPrice(rg.nextInt(1, 200)/10.0)
                    .build()
            ).toList();

        List<Product> persisted = this.saveAll(products);

        return persisted;
    }

    private static String getPredefinedProductName(int idx) {
        String[] productNames = {
            "bread", "milk", "butter", "chocolate", "apple", "tomato", "biscuit", "peanut", "yoghurt", "chips",
            "carrot", "honey", "cheese", "banana", "tomato juice", "olives", "CocaCola", "tea", "jam"
        };
    
        if (idx < productNames.length) {
            return productNames[idx];
        }

        return productNames[idx % productNames.length] + idx;
    }
}
