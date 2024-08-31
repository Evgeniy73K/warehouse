package org.mediasoft.warehouse.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mediasoft.warehouse.controller.dto.RequestCriteriaDto;
import org.mediasoft.warehouse.service.dto.CriteriaDto;

import java.util.List;

@Mapper
public interface CriteriaMapper {
    CriteriaMapper INSTANCE = Mappers.getMapper(CriteriaMapper.class);

    List<CriteriaDto> toCriteriaDto(List<RequestCriteriaDto> requestCriteriaDto);
}
