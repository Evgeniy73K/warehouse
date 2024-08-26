package org.mediasoft.warehouse.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.mediasoft.warehouse.service.enums.SearchEnum;

@Data
@AllArgsConstructor
public class SearchDto {
    private String field;
    private Object value;
    private SearchEnum operation;
}
