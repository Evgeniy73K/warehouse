package org.mediasoft.warehouse.controller.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestCreateOrderDto { //swagger

    @Size(min = 4, max = 255, message = "deliveryAddress must be between 4 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "deliveryAddress must not contain special characters")
    @NotBlank
    private String deliveryAddress;
    @NotNull(message = "products must not be null")
    @Valid
    private List<ProductSummaryDto> products;

}
