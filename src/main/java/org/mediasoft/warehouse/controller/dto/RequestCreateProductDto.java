package org.mediasoft.warehouse.controller.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestCreateProductDto {
    @Size(min = 4, max = 20, message = "Name must be between 4 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Name must not contain special characters")
    private String name;

    @NotNull(message = "Article must not be null")
    private UUID article;

    @Size(min = 4, max = 20, message = "Category must be between 4 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Category must not contain special characters")
    private String category;

    @Size(min = 4, max = 500, message = "Dictionary must be between 4 and 500 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Dictionary must not contain special characters")
    private String dictionary;

    @DecimalMin(value = "1.0", inclusive = true, message = "Price must be at least 1")
    private BigDecimal price;

    @DecimalMin(value = "1.0", inclusive = true, message = "Qty must be at least 1")
    private BigDecimal qty;

}
