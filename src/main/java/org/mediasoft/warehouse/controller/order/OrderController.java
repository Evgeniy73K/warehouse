package org.mediasoft.warehouse.controller.order;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mediasoft.warehouse.controller.order.dto.OrderInfoDto;
import org.mediasoft.warehouse.controller.order.dto.RequestCreateOrderDto;
import org.mediasoft.warehouse.controller.order.dto.RequestUpdateOrderDto;
import org.mediasoft.warehouse.mappers.OrderMapper;
import org.mediasoft.warehouse.service.order.dto.ChangeStatusDto;
import org.mediasoft.warehouse.service.order.dto.GetOrderDto;
import org.mediasoft.warehouse.service.order.impl.OrderInfoServiceImpl;
import org.mediasoft.warehouse.service.order.impl.OrderServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderServiceImpl orderService;
    private final OrderInfoServiceImpl orderInfoService;


    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createOrder(@RequestBody @Valid RequestCreateOrderDto requestCreateOrderDto,
                            @RequestHeader("customer_id") Long customerId) {
        orderService.createOrder(OrderMapper.INSTANCE.toCreateOrderDto(requestCreateOrderDto), customerId);
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateOrder(@RequestBody @Valid RequestUpdateOrderDto requestUpdateOrderDto, @PathVariable("id") UUID id,
                            @RequestHeader("customer_id") Long customerId) {
        orderService.updateOrder(OrderMapper.INSTANCE.toUpdateOrderDto(requestUpdateOrderDto), id, customerId);
    }

    @DeleteMapping("{id}")
    public void deleteOrder(@PathVariable("id") UUID id, @RequestHeader("customer_id") Long customerId) {
        orderService.deleteOrder(id, customerId);
    }

    @PatchMapping("{id}/status")
    public void changeStatus(@PathVariable("id") UUID id, @RequestBody ChangeStatusDto changeStatusDto) {
        orderService.changeStatus(id, changeStatusDto);
    }

    @GetMapping("{id}")
    public GetOrderDto getOrder(@PathVariable("id") UUID id, @RequestHeader("customer_id") Long customerId) {
        return orderService.getOrder(id, customerId);
    }

    @GetMapping("info/{productId}")
    public HashMap<UUID, List<OrderInfoDto>> getOrderInfo(@PathVariable("productId") UUID productId) {
        return orderInfoService.getOrderInfo(productId);
    }
}
