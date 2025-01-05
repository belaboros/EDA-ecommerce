package org.msffp.product.model;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min=2, max=20)
    @Column(length=20)
    private String name;

    @DecimalMin(value="0.1")
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

    @Version
    private long version;
}
