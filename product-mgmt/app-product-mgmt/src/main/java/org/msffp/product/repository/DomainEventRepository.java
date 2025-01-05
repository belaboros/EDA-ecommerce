package org.msffp.product.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import org.msffp.product.domainevent.DomainEvent;


public interface DomainEventRepository extends JpaRepository<DomainEvent, UUID> {

}
