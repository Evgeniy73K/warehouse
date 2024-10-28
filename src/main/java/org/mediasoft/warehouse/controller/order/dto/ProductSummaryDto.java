package org.mediasoft.warehouse.controller.order.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ProductSummaryDto {
    @NotNull(message = "id must not be null")
    private UUID id;
    @DecimalMin(value = "1.0", inclusive = true, message = "Qty must be at least 1")
    private BigDecimal qty;
}
