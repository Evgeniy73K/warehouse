package org.mediasoft.warehouse.controller.order.dto;

import jakarta.validation.constraints.DecimalMax;
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
    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    @NotNull
    private BigDecimal qty;
}
