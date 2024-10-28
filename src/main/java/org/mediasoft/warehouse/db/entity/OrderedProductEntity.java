package org.mediasoft.warehouse.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mediasoft.warehouse.db.entity.keys.OrderedProductId;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "ordered_product")
@Builder
public class OrderedProductEntity {
    @EmbeddedId
    private OrderedProductId id;

    @Column(name = "qty", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

}