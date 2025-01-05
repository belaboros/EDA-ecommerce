package org.msffp.order.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import org.msffp.order.domainevent.DomainEvent;


public interface DomainEventRepository extends JpaRepository<DomainEvent, UUID> {
}
