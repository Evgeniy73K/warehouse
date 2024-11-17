package org.mediasoft.warehouse.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mediasoft.warehouse.controller.order.dto.RequestCreateOrderDto;
import org.mediasoft.warehouse.controller.order.dto.RequestUpdateOrderDto;
import org.mediasoft.warehouse.service.order.dto.CreateOrderDto;
import org.mediasoft.warehouse.service.order.dto.UpdateOrderDto;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    CreateOrderDto toCreateOrderDto(RequestCreateOrderDto requestCreateOrderDto);

    UpdateOrderDto toUpdateOrderDto(RequestUpdateOrderDto requestUpdateOrderDto);
}

