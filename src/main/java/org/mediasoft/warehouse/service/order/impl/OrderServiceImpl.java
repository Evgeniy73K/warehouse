package org.mediasoft.warehouse.service.order.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mediasoft.warehouse.db.entity.OrderEntity;
import org.mediasoft.warehouse.db.entity.OrderedProductEntity;
import org.mediasoft.warehouse.db.entity.ProductEntity;
import org.mediasoft.warehouse.db.entity.enums.OrderStatusEnum;
import org.mediasoft.warehouse.db.entity.keys.OrderedProductId;
import org.mediasoft.warehouse.db.projection.OrderSummary;
import org.mediasoft.warehouse.db.repository.OrderRepository;
import org.mediasoft.warehouse.db.repository.OrderedProductRepository;
import org.mediasoft.warehouse.db.repository.ProductRepository;
import org.mediasoft.warehouse.error.exception.BusinessException;
import org.mediasoft.warehouse.service.order.OrderService;
import org.mediasoft.warehouse.service.order.dto.ChangeStatusDto;
import org.mediasoft.warehouse.service.order.dto.CreateOrderDto;
import org.mediasoft.warehouse.service.order.dto.GetOrderDto;
import org.mediasoft.warehouse.service.order.dto.ProductSummaryDto;
import org.mediasoft.warehouse.service.order.dto.UpdateOrderDto;
import org.mediasoft.warehouse.service.order.utils.OrderDataGetter;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import static org.mediasoft.warehouse.error.code.CommonErrorEnum.IMPOSSIBLE_DELETE_ORDER;
import static org.mediasoft.warehouse.error.code.CommonErrorEnum.IMPOSSIBLE_UPDATE_ORDER;
import static org.mediasoft.warehouse.error.code.CommonErrorEnum.ORDER_NOT_FOUND;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderedProductRepository orderedProductRepository;
    private final OrderDataGetter orderDataGetter;

    @Override
    @Transactional
    public void createOrder(List<ProductSummaryDto> products, String deliveryAddress, Long customerId) {
        var customer = orderDataGetter.getUser(customerId);
        var productsEntities = new HashSet<>(orderDataGetter.getProducts(products));

        var orderEntity = OrderEntity.builder()
                .deliveryAddress(deliveryAddress)
                .customer(customer)
                .build();

        saveOrUpdateOrder(products, productsEntities, orderEntity, false);

        log.info("ЗАКАЗ СОЗДАН!!!!!!!!!! {}", orderEntity.getId());
    }

    @Override
    @Transactional
    public void updateOrder(List<ProductSummaryDto> products, UUID orderId, Long customerId) {
        if (customerId == null) throw new BusinessException();

        var orderEntity = orderRepository.findById(orderId).stream()
                .findFirst()
                .orElseThrow(
                        () -> new NoSuchElementException(ORDER_NOT_FOUND.getMessage()));
        if (!orderEntity.getCustomer().getId().equals(customerId))
            throw new BusinessException();

        if (!orderEntity.getStatus().equals(OrderStatusEnum.CREATED)) {
            throw new RuntimeException(IMPOSSIBLE_UPDATE_ORDER.getMessage());
        }

        var productsEntities = new HashSet<>(orderDataGetter.getProducts(products));

        saveOrUpdateOrder(products, productsEntities, orderEntity, true);
        log.info("ЗАКАЗ ОБНОВЛЕН!!!!!!!!!! {}", orderEntity.getId());
    }

    @Override
    @Transactional
    public void deleteOrder(UUID orderId, Long customerId) {
        if (customerId == null) throw new BusinessException();

        var orderEntity = orderRepository.findById(orderId).stream()
                .findFirst()
                .orElseThrow(
                        () -> new NoSuchElementException(ORDER_NOT_FOUND.getMessage()));
        if (!orderEntity.getCustomer().getId().equals(customerId))
            throw new BusinessException();

        if (!orderEntity.getStatus().equals(OrderStatusEnum.CREATED)) {
            throw new RuntimeException(IMPOSSIBLE_DELETE_ORDER.getMessage());
        }

        orderEntity.setStatus(OrderStatusEnum.CANCELLED);

        var orderedProductList = orderedProductRepository.findAllByOrderId(orderId);


        List<ProductEntity> rollbackProducts = orderedProductList.stream()
                .map(o -> productRepository.findById(o.getId().getProduct().getId()) //merge
                        .map(p -> {
                                    p.setQty(p.getQty().add(o.getQuantity()));
                                    return p;
                                }
                        )
                        .orElseThrow(RuntimeException::new)
                ).toList(); //Сделаю красиво


        orderedProductRepository.deleteAll(orderedProductList); //лишние
        orderRepository.save(orderEntity);
        productRepository.saveAll(rollbackProducts);

        log.info("ЗАКАЗ ОТМЕНЕН!!!!!!!!!! {}", orderEntity.getId());

    }

    @Override
    public void confirmOrder(UUID orderId, Long customerId) {
        //todo

    }

    @Override
    public void changeStatus(UUID orderId, OrderStatusEnum orderStatusEnum) {
        var orderEntity = orderRepository.findById(orderId).orElseThrow(
                () -> new NoSuchElementException(ORDER_NOT_FOUND.getMessage())
        );

        orderEntity.setStatus(orderStatusEnum);
        orderRepository.save(orderEntity);

        log.info("СТАТУС ЗАКАЗА ИЗМЕНЕН!!!!!!!!!! {}", orderEntity.getId());
    }

    @Override
    public GetOrderDto getOrder(UUID orderId, Long customerId) {
        if (customerId == null) throw new BusinessException();

        var orderEntity = orderRepository.findById(orderId).orElseThrow(
                () -> new NoSuchElementException(ORDER_NOT_FOUND.getMessage())
        );

        if (!orderEntity.getCustomer().getId().equals(customerId)) throw new BusinessException();

        var orderSummary = productRepository.getOrderDetailByOrderId(orderId);

        var totalPrice = orderSummary.stream()
                .map(OrderSummary::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return GetOrderDto.builder()
                .orderId(orderId)
                .products(orderSummary)
                .totalPrice(totalPrice)
                .build();
    }


    private void saveOrUpdateOrder(List<ProductSummaryDto> products, Set<ProductEntity> productsEntities,
                                   OrderEntity orderEntity,
                                   Boolean isUpdate) {

        List<OrderedProductEntity> orderedProductEntityList = new ArrayList<>();

        products.forEach(product -> {
            var productEntity = productsEntities.stream()
                    .filter(p -> p.getId().equals(product.getId()))
                    .findFirst()
                    .orElseThrow(NoSuchElementException::new);

            var orderedProductId = new OrderedProductId(orderEntity, productEntity);

            if (isUpdate) {
                orderedProductRepository.findById(orderedProductId).ifPresent(orderedProductEntity ->
                        productEntity.setQty(productEntity.getQty().add(orderedProductEntity.getQuantity()))
                );
            }

            var orderedProductEntity = OrderedProductEntity.builder()
                    .id(orderedProductId)
                    .price(productEntity.getPrice())
                    .quantity(product.getQty())
                    .build();
            orderedProductEntityList.add(orderedProductEntity);
            productEntity.setQty(productEntity.getQty().subtract(product.getQty()));

        });

        orderRepository.save(orderEntity);
        orderedProductRepository.saveAll(orderedProductEntityList);
        productRepository.saveAll(productsEntities);
    }
}