package org.mediasoft.warehouse.controller.order;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mediasoft.warehouse.controller.order.dto.RequestCreateOrderDto;
import org.mediasoft.warehouse.controller.order.dto.RequestUpdateOrderDto;
import org.mediasoft.warehouse.mappers.OrderMapper;
import org.mediasoft.warehouse.service.order.dto.ChangeStatusDto;
import org.mediasoft.warehouse.service.order.dto.CreateOrderDto;
import org.mediasoft.warehouse.service.order.dto.GetOrderDto;
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

import java.util.UUID;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderServiceImpl orderService;


    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createOrder(@RequestBody @Valid RequestCreateOrderDto requestCreateOrderDto,
                            @RequestHeader("customer_id") Long customerId) {
        final CreateOrderDto createOrderDto = OrderMapper.INSTANCE.toCreateOrderDto(requestCreateOrderDto);

        orderService.createOrder(createOrderDto.getProducts(), createOrderDto.getDeliveryAddress(), customerId);
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateOrder(@RequestBody @Valid RequestUpdateOrderDto requestUpdateOrderDto, @PathVariable("id") UUID id,
                            @RequestHeader("customer_id") Long customerId) {
        var products = OrderMapper.INSTANCE.toUpdateOrderDto(requestUpdateOrderDto).getProducts();

        orderService.updateOrder(products, id, customerId);
    }

    @DeleteMapping("{id}")
    public void deleteOrder(@PathVariable("id") UUID id, @RequestHeader("customer_id") Long customerId) {
        orderService.deleteOrder(id, customerId);
    }

    @PatchMapping("{id}/status")
    public void changeStatus(@PathVariable("id") UUID id, @RequestBody ChangeStatusDto changeStatusDto) {
        orderService.changeStatus(id, changeStatusDto.getStatus());
    }

    @GetMapping("{id}")
    public GetOrderDto getOrder(@PathVariable("id") UUID id, @RequestHeader("customer_id") Long customerId) {
        return orderService.getOrder(id, customerId);
    }
}
