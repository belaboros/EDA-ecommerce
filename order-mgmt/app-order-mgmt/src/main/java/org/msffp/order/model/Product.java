package org.msffp.order.model;

import java.io.Serializable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.DecimalMin;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor(access=AccessLevel.PUBLIC)
@AllArgsConstructor(access=AccessLevel.PUBLIC)
@Builder
@Entity
@Table(name = "`product`")
public class Product implements Serializable {
    @Id
    @NotNull
    private Long id;

    @NotNull
    @Size(min=2, max=20)
    @Column(length=20, nullable=false)  // @NotNull was not enough, force CREATE TABLE DDL to prevent NULL name
    private String name;

    @DecimalMin(value="0.1")
    @NotNull
    @Column(nullable=false)     // @NotNull was not enough, force CREATE TABLE DDL to prevent NULL unitPrice
    private Double unitPrice;

    /**
     * Is this product still active.
     * NOTE: non-active (a.k.a) products 
     *  - cannot be ordered any more
     *  - cannot be activated any more.
     */
    @Builder.Default
    @Setter(AccessLevel.NONE)
    private boolean active = true;

    public void passivate() {
        active = false;
    }

    @NotNull
    @Version
    private long version;

    public static Product fromJson(String json) throws ProductParsingException {
        ObjectMapper mapper = new ObjectMapper();
        
        // consume messages produced by newer ProductMgmt app and ignore additional/unknown attributes
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            JsonNode node = mapper.readTree(json);
            JsonNode payload = node.get("payload");
            if (payload == null) {
                throw new ProductParsingException("Invalid Product JSON message. Missing \"payload\" attribute: " + json);
            }

            try {
                Product product = mapper.readValue(payload.toString(), Product.class);
                if (product.getId() == null) {
                    throw new ProductParsingException("Invalid product json. ID attribute is missing: " + json);
                }
                return product;
            } catch (JsonProcessingException e) {
                throw new ProductParsingException("Invalid Product payload: " + payload.toString(), e);
            }
        } catch (JsonProcessingException e) {
            throw new ProductParsingException("Invalid Product JSON: " + json, e);
        }
    }
}

