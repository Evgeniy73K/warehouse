package org.mediasoft.warehouse.service.order;

import org.mediasoft.warehouse.service.order.dto.ChangeStatusDto;
import org.mediasoft.warehouse.service.order.dto.CreateOrderDto;
import org.mediasoft.warehouse.service.order.dto.GetOrderDto;
import org.mediasoft.warehouse.service.order.dto.UpdateOrderDto;

import java.util.UUID;

public interface OrderService {
    void createOrder(CreateOrderDto createOrderDto, Long customerId);

    void updateOrder(UpdateOrderDto updateOrderDto, UUID orderId, Long customerId);

    void deleteOrder(UUID orderId, Long customerId);

    void confirmOrder(UUID orderId, Long customerId);

    void changeStatus(UUID orderId, ChangeStatusDto changeStatusDto);

    GetOrderDto getOrder(UUID orderId, Long customerId);
}
