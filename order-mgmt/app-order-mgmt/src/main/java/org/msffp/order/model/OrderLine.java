package org.msffp.order.model;

import org.json.JSONPropertyIgnore;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor(access=AccessLevel.PUBLIC)
@AllArgsConstructor(access=AccessLevel.PUBLIC)
@Builder
@Entity
@Table(name = "`order_line`")
public class OrderLine {

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private Long productID;    

    @Min(1)
    private Integer quantity;

    @NotNull
    private Double unitPriceAtShopping;

    @ToString.Exclude
    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private Order order;

    @JSONPropertyIgnore
    public Order getOrder() {
        return order;
    }
}
