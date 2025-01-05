package org.msffp.order.model;

import java.beans.Transient;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONPropertyIgnore;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access=AccessLevel.PUBLIC)
@AllArgsConstructor(access=AccessLevel.PUBLIC)
@Builder
@Entity
@Table(name = "`order`")
public class Order implements Serializable {
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @NotBlank
    private Integer customerID;

    @NotNull
    @NotBlank
    private LocalDate deliveryDay;

    @Builder.Default
    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "order", fetch = FetchType.EAGER)
    private List<OrderLine> lineItems = new ArrayList<>();

    @Min(0)
    private Double totalPrice;

    @Builder.Default
    private OrderStatus status = OrderStatus.CREATED;

    @Version
    private long version;


    @Transient
    @JSONPropertyIgnore
    public boolean isNew() {
        return this.id == null;
    }

    public Order addOrderLine(OrderLine ol) {
        if (ol.getOrder() != null) {
            throw new IllegalArgumentException("The OrderLine has already a non-null order: " + ol.toString());
        }

        lineItems.add(ol);
        ol.setOrder(this);

        return this;
    }
}
