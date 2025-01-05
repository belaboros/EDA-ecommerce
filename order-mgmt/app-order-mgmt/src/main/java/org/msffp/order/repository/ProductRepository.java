package org.msffp.order.repository;

import org.msffp.order.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/// Product repository to create local replica which contains all the latest product records.
/// Ideal to receive domain event messages via un-reliable/un-ordered messaging channel
/// where Product messages can be lost, duplicated or come out-of-order.
/// 
/// Features:
///  - idempotent:
///    ignores identical modifications
///    e.g.: from duplicate Product:* domain event messages
///  - ignores outdated modifications with a lower version attributes 
///    e.g.: from an out-of-order/late domain event messages
///  - can tolerate missing messages and out-of-order/erly messages after
///    where the version attribute is bigger then the version attribute of the local replica.   
/// 
/// The native prostgresql UPSERT/MERGE command:
/// ```
/// INSERT INTO public.product AS p
///        (id, name, unit_price, active, version).
///        VALUES (?, ?, ?, ?, ?)
///     ON CONFLICT (id) DO UPDATE.
///     SET
///         name = EXCLUDED.name,
///         unit_price = EXCLUDED.unit_price,
///         active = EXCLUDED.active,
///         version = EXCLUDED.version+1
///         WHERE p.version<=EXCLUDED.version AND (unit_price<>EXCLUDED.unit_price OR active<>EXCLUDED.active);
/// ```
/// 
/// See also
///  - https://www.postgresql.org/docs/current/sql-insert.html
///  - https://www.baeldung.com/spring-data-jpa-update-or-insert
///  - https://vladmihalcea.com/hibernate-on-conflict-do-clause/
///  - https://hackernoon.com/spring-and-postgresql-make-your-database-inserts-30-times-faster
///  - https://www.baeldung.com/hibernate-insert-query-on-conflict-clause
/// 
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {


    @Modifying
    @Query("INSERT INTO Product AS e " + 
                "(id, name, unitPrice, active, version) " + 
                "VALUES (:id, :name, :unitPrice, :active, :version) " + 
                "ON CONFLICT (id) " + 
                "DO UPDATE " + 
                "SET " + 
                "name = :name, " + 
                "unitPrice = :unitPrice," + 
                "active = :active," + 
                "version = :version " + 
                "WHERE e.version<:version"
    )
    int upsertWithForwardVersion(@Param("id") Long id, @Param("name") String name, @Param("unitPrice") double unitPrice, @Param("active") boolean active, @Param("version") long version);
}
