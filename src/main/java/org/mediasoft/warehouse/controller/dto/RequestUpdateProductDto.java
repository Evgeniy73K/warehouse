package org.mediasoft.warehouse.controller.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class RequestUpdateProductDto {

    @Size(max = 255, message = "Name cannot be longer than 255 characters")
    private String name;

    @NotNull(message = "ID cannot be null")
    private UUID id;

    private UUID article;

    @Pattern(regexp = "^[A-Z_]*$", message = "Category must be in uppercase and can only contain letters and underscores")
    private String category;

    @Size(max = 65535, message = "Dictionary cannot be longer than 65535 characters")
    private String dictionary;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be higher than 0")
    private BigDecimal price;

    @DecimalMin(value = "0.0", inclusive = true, message = "Quantity cannot be negative")
    private BigDecimal qty;
}


