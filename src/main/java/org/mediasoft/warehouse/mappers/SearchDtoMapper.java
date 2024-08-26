package org.mediasoft.warehouse.mappers;

import org.mediasoft.warehouse.controller.dto.RequestSearchDto;
import org.mediasoft.warehouse.service.dto.SearchDto;
import org.mediasoft.warehouse.service.enums.SearchEnum;

import java.util.ArrayList;
import java.util.List;

import static org.mediasoft.warehouse.service.enums.SearchEnum.EQUAL;
import static org.mediasoft.warehouse.service.enums.SearchEnum.GRATER_THAN_OR_EQ;
import static org.mediasoft.warehouse.service.enums.SearchEnum.LESS_THAN_OR_EQ;
import static org.mediasoft.warehouse.service.enums.SearchEnum.LIKE;

public class SearchDtoMapper {
    public static List<SearchDto> toSearchDto(List<RequestSearchDto> requestSearchDto) {
        List<SearchDto> searchDtos = new ArrayList<>();

        requestSearchDto.forEach(r -> {
            SearchEnum operation;

            var field = r.getField();
            var value = r.getValue();

            if (r.getOperation().equals(EQUAL.getValue()) ||
                    r.getOperation().equals(GRATER_THAN_OR_EQ.getValue()) ||
                    r.getOperation().equals(LESS_THAN_OR_EQ.getValue()) ||
                    r.getOperation().equals(LIKE.getValue())) {
                operation = SearchEnum.fromValue(r.getOperation());
            } else {
                operation = SearchEnum.valueOf(r.getOperation());
            }

            var searchDto = new SearchDto(field, value, operation);
            searchDtos.add(searchDto);

        });

        return searchDtos;
    }
}
