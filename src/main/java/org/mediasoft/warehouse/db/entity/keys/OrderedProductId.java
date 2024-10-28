package org.mediasoft.warehouse.db.entity.keys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderedProductId {


    @NotNull
    @Column(name = "order_id")
    private UUID orderId;

    @NotNull
    @Column(name = "product_id")
    private UUID productId;
}
