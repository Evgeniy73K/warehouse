package org.mediasoft.warehouse.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mediasoft.warehouse.controller.dto.RequestCreateProductDto;
import org.mediasoft.warehouse.controller.dto.RequestSearchDto;
import org.mediasoft.warehouse.controller.dto.RequestUpdateProductDto;
import org.mediasoft.warehouse.controller.dto.ResponseProductDto;
import org.mediasoft.warehouse.db.entity.ProductEntity;
import org.mediasoft.warehouse.service.dto.CreateProductDto;
import org.mediasoft.warehouse.service.dto.SearchDto;
import org.mediasoft.warehouse.service.dto.UpdateProductDto;

import java.util.List;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductEntity toProductEntity(CreateProductDto createProductDto);

    CreateProductDto toCreateProductDto(RequestCreateProductDto requestCreateProductDto);

    UpdateProductDto toUpdateProductDto(RequestUpdateProductDto requestUpdateProductDto);

    ResponseProductDto toResponseProductDto(ProductEntity productEntity);
}

