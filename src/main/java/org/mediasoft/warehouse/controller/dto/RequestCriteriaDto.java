package org.mediasoft.warehouse.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestCriteriaDto {
    @NotNull(message = "field cannot be null")
    private String field;
    @NotNull(message = "value cannot be null")
    private Object value;
    @NotNull(message = "field cannot be null")
    @Pattern(regexp = "^(=|>=|<=|~|EQUAL|GRATER_THAN_OR_EQ|LESS_THAN_OR_EQ|LIKE)$", message = "Invalid operation")
    private String operation;
}
