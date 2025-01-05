package org.msffp.product.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.msffp.product.model.Product;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
