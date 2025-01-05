package org.msffp.product.domainevent;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.json.JSONObject;
import org.msffp.product.model.Product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Domain event entity class to publish business event messages for subscribing consumers.
 * See also
 *  - https://debezium.io/documentation/reference/transformations/outbox-event-router.html
 *  - https://github.com/debezium/debezium-examples/blob/main/outbox/outbox-overview.png
 *  - https://www.baeldung.com/spring-boot-jpa-storing-postgresql-jsonb This is used !!!
 *	- (https://github.com/vladmihalcea/hypersistence-utils this is NOT used !!!)
 * By the way, from Hibernate 6.0 there’s an annotation @JdbcTypeCode, 
 * which does the same as @TypeDef + @Type from ‘hypersistence-utils-hibernate’ library – 
 * could be used like @JdbcTypeCode(SqlTypes.JSON), 
 * and it works with JSONB perfectly 
 * if you also have @Column(columnDefinition = "jsonb") above the entity field.
 * It’s more convenient to use, 
 * because when you’d like to tune a mapper 
 * which is used under the hood for ‘hypersistence-utils-hibernate’, 
 * you have to create a configuration file to point the lib to your custom mapper configuration, 
 * but for Hibernate 6.0 there’s a cool feature 
 * to pass this config via application property spring.jpa.properties.hibernate.json_format_mapper
 */
@NoArgsConstructor
@Getter
@Setter
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@Table(name="outbox")
public class DomainEvent {
    @Id
    @NotNull
    private UUID eventId;

    @NotNull
    @NotBlank
    @Column(length = 255)
    @Size(max = 255)
    private String aggregateType;


    @NotNull
    @NotBlank
    @Column(length = 255)
    @Size(max = 255)    
    private String aggregateId;

    @NotNull
    @NotBlank
    @Column(length = 255)
    @Size(max = 255)
    private String eventType;

    @NotNull
    private LocalDateTime createdAt;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String payload;

    /**
     * - TracingSpanContext
     */
    private String customHeaders;


    public DomainEvent(String aggregateId) {
        if (aggregateId == null) {
            throw new IllegalArgumentException("Expected a non-null aggregate ID, but received null.");
        }

        eventId = UUID.randomUUID();
        this.aggregateId = aggregateId;
        //createdAt = ZonedDateTime.now(ZoneId.of("UTC"));
        createdAt = LocalDateTime.now(ZoneId.of("UTC"));
    }

    public static DomainEvent create(Product p) {
        return create(p, "Changed", null);
    }

    public static DomainEvent create(Product o, String domainEventType, String customHeaders) {
        DomainEvent event = new DomainEvent(Long.toString(o.getId()));
        event.setAggregateType(Product.class.getSimpleName());
        event.setEventType(domainEventType);
        event.setCustomHeaders(customHeaders);
        event.setPayload(new JSONObject(o).toString(4));
        return event;
    }    
}
