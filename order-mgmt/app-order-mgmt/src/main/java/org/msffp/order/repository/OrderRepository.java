package org.msffp.order.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.msffp.order.model.Order;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
