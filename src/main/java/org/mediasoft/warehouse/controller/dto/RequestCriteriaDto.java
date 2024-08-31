package org.mediasoft.warehouse.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.mediasoft.warehouse.service.enums.CriteriaEnum;

@Data
@AllArgsConstructor
public class RequestCriteriaDto {
    @NotNull(message = "field cannot be null")
    private String field;
    @NotNull(message = "value cannot be null")
    private Object value;
    @NotNull(message = "field cannot be null")
    private CriteriaEnum operation;
}
