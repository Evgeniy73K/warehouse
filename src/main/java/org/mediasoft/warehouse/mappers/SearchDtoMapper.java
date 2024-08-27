package org.mediasoft.warehouse.mappers;

import org.mediasoft.warehouse.controller.dto.RequestCriteriaDto;
import org.mediasoft.warehouse.service.dto.CriteriaDto;
import org.mediasoft.warehouse.service.enums.CriteriaEnum;

import java.util.ArrayList;
import java.util.List;

import static org.mediasoft.warehouse.service.enums.CriteriaEnum.EQUAL;
import static org.mediasoft.warehouse.service.enums.CriteriaEnum.GRATER_THAN_OR_EQ;
import static org.mediasoft.warehouse.service.enums.CriteriaEnum.LESS_THAN_OR_EQ;
import static org.mediasoft.warehouse.service.enums.CriteriaEnum.LIKE;

public class SearchDtoMapper {
    public static List<CriteriaDto> toSearchDto(List<RequestCriteriaDto> requestCriteriaDto) {
        List<CriteriaDto> criteriaDtos = new ArrayList<>();

        requestCriteriaDto.forEach(r -> {
            CriteriaEnum operation;

            var field = r.getField();
            var value = r.getValue();

            if (r.getOperation().equals(EQUAL.getValue()) ||
                    r.getOperation().equals(GRATER_THAN_OR_EQ.getValue()) ||
                    r.getOperation().equals(LESS_THAN_OR_EQ.getValue()) ||
                    r.getOperation().equals(LIKE.getValue())) {
                operation = CriteriaEnum.fromValue(r.getOperation());
            } else {
                operation = CriteriaEnum.valueOf(r.getOperation());
            }

            var searchDto = new CriteriaDto(field, value, operation);
            criteriaDtos.add(searchDto);

        });

        return criteriaDtos;
    }
}
