package org.mediasoft.warehouse.controller.order;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mediasoft.warehouse.controller.order.dto.RequestCreateOrderDto;
import org.mediasoft.warehouse.controller.order.dto.RequestUpdateOrderDto;
import org.mediasoft.warehouse.mappers.OrderMapper;
import org.mediasoft.warehouse.service.order.dto.ChangeStatusDto;
import org.mediasoft.warehouse.service.order.dto.GetOrderDto;
import org.mediasoft.warehouse.service.order.impl.OrderServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    @ResponseStatus(HttpStatus.CREATED)
    public void createOrder(@RequestBody @Valid RequestCreateOrderDto requestCreateOrderDto) {

        orderService.createOrder(OrderMapper.INSTANCE.toCreateOrderDto(requestCreateOrderDto));
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateOrder(@RequestBody @Valid RequestUpdateOrderDto requestUpdateOrderDto, @PathVariable("id") UUID id) {
        orderService.updateOrder(OrderMapper.INSTANCE.toUpdateOrderDto(requestUpdateOrderDto), id);
    }

    @DeleteMapping("{id}")
    public void deleteOrder(@PathVariable("id") UUID id) {
        orderService.deleteOrder(id);
    }

    @PatchMapping("{id}/status")
    public void changeStatus(@PathVariable("id") UUID id, @RequestBody ChangeStatusDto changeStatusDto) {
        orderService.changeStatus(id, changeStatusDto);
    }

    @GetMapping("{id}")
    public GetOrderDto getOrder(@PathVariable("id") UUID id) {
        return orderService.getOrder(id);
    }
}
