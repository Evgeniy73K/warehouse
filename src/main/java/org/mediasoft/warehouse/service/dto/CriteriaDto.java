package org.mediasoft.warehouse.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.mediasoft.warehouse.service.enums.CriteriaEnum;

@Data
@AllArgsConstructor
public class CriteriaDto {
    private String field;
    private Object value;
    private CriteriaEnum operation;
}
