package org.mediasoft.warehouse.service.order;

import org.mediasoft.warehouse.controller.order.dto.OrderInfoDto;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public interface OrderInfoService {
    HashMap<UUID, List<OrderInfoDto>> getOrderInfo(UUID productId);
}
