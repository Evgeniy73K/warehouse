package org.mediasoft.warehouse.service.order.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.mediasoft.warehouse.db.projection.OrderSummary;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Setter
@Builder
public class GetOrderDto {
    private UUID orderId;
    private List<OrderSummary> products;
    private BigDecimal totalPrice;
}
