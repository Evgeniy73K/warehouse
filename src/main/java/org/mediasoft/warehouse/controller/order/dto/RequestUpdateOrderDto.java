package org.mediasoft.warehouse.controller.order.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestUpdateOrderDto {
    @NotNull(message = "products must not be null")
    private List<ProductSummaryDto> products;
}
