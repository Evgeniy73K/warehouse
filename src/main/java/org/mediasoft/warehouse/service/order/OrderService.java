package org.mediasoft.warehouse.service.order;

import org.mediasoft.warehouse.db.entity.enums.OrderStatusEnum;
import org.mediasoft.warehouse.service.order.dto.ChangeStatusDto;
import org.mediasoft.warehouse.service.order.dto.CreateOrderDto;
import org.mediasoft.warehouse.service.order.dto.GetOrderDto;
import org.mediasoft.warehouse.service.order.dto.ProductSummaryDto;
import org.mediasoft.warehouse.service.order.dto.UpdateOrderDto;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    void createOrder(List<ProductSummaryDto> products, String deliveryAddress, Long customerId);

    void updateOrder(List<ProductSummaryDto> products, UUID orderId, Long customerId);

    void deleteOrder(UUID orderId, Long customerId);

    void confirmOrder(UUID orderId, Long customerId);

    void changeStatus(UUID orderId, OrderStatusEnum orderStatusEnum);

    GetOrderDto getOrder(UUID orderId, Long customerId);
}
