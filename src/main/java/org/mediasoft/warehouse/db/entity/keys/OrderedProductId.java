package org.mediasoft.warehouse.db.entity.keys;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mediasoft.warehouse.db.entity.OrderEntity;
import org.mediasoft.warehouse.db.entity.ProductEntity;


@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderedProductId {


    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private OrderEntity orderId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private ProductEntity product;
}
