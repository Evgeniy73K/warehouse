package org.mediasoft.warehouse.service.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class ProductSummaryDto {

    private UUID id;
    private BigDecimal qty;

}
