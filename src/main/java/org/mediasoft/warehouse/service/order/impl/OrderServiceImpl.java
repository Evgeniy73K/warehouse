package org.mediasoft.warehouse.service.order.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mediasoft.warehouse.db.entity.OrderEntity;
import org.mediasoft.warehouse.db.entity.OrderedProductEntity;
import org.mediasoft.warehouse.db.entity.ProductEntity;
import org.mediasoft.warehouse.db.entity.enums.StatusEnum;
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
import org.mediasoft.warehouse.service.order.utils.OrderValidator;
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
    private final OrderValidator orderValidator;
    private final HttpServletRequest request;
    private final static String HEADER_CUSTOMER_ID = "customer_id";

    @Override
    @Transactional
    public void createOrder(CreateOrderDto createOrderDto) {
        var customerId = Long.parseLong(request.getHeader(HEADER_CUSTOMER_ID));
        var customer = orderValidator.validateUser(customerId);
        var products = createOrderDto.getProducts();
        var productsEntities = new HashSet<>(orderValidator.validateProduct(products));

        var orderEntity = OrderEntity.builder()
                .deliveryAddress(createOrderDto.getDeliveryAddress())
                .customer(customer)
                .build();

        saveOrUpdateOrder(products, productsEntities, orderEntity, false);


        log.info("ЗАКАЗ СОЗДАН!!!!!!!!!! {}", orderEntity.getId());
    }

    @Override
    @Transactional
    public void updateOrder(UpdateOrderDto updateOrderDto, UUID orderId) {
        if (request.getHeader(HEADER_CUSTOMER_ID) == null) throw new BusinessException();

        var orderEntity = orderRepository.findById(orderId).stream()
                .findFirst()
                .orElseThrow(
                        () -> new NoSuchElementException(ORDER_NOT_FOUND.getMessage()));
        if (!orderEntity.getCustomer().getId().toString().equals(request.getHeader(HEADER_CUSTOMER_ID)))
            throw new BusinessException();

        if (!orderEntity.getStatus().equals(StatusEnum.CREATED)) {
            throw new RuntimeException(IMPOSSIBLE_UPDATE_ORDER.getMessage());
        }

        var products = updateOrderDto.getProducts();
        var productsEntities = new HashSet<>(orderValidator.validateProduct(products));

        saveOrUpdateOrder(products, productsEntities, orderEntity, true);
        log.info("ЗАКАЗ ОБНОВЛЕН!!!!!!!!!! {}", orderEntity.getId());
    }

    @Override
    @Transactional
    public void deleteOrder(UUID orderId) {
        if (request.getHeader(HEADER_CUSTOMER_ID) == null) throw new BusinessException();

        var orderEntity = orderRepository.findById(orderId).stream()
                .findFirst()
                .orElseThrow(
                        () -> new NoSuchElementException(ORDER_NOT_FOUND.getMessage()));
        if (!orderEntity.getCustomer().getId().toString().equals(request.getHeader(HEADER_CUSTOMER_ID)))
            throw new BusinessException();

        if (!orderEntity.getStatus().equals(StatusEnum.CREATED)) {
            throw new RuntimeException(IMPOSSIBLE_DELETE_ORDER.getMessage());
        }

        orderEntity.setStatus(StatusEnum.CANCELLED);

        var orderedProductList = orderedProductRepository.findAllByOrderId(orderId);


        List<ProductEntity> rollbackProducts = new ArrayList<>();

        orderedProductList.forEach(o -> {
            productRepository.findById(o.getId().getProductId())
                    .ifPresent(p -> {
                        p.setQty(p.getQty().add(o.getQuantity()));
                        rollbackProducts.add(p);
                    });
        });

        orderedProductRepository.deleteAll(orderedProductList);
        orderRepository.save(orderEntity);
        productRepository.saveAll(rollbackProducts);

        log.info("ЗАКАЗ ОТМЕНЕН!!!!!!!!!! {}", orderEntity.getId());

    }

    @Override
    public void confirmOrder(UUID orderId) {
        var customerId = request.getHeader(HEADER_CUSTOMER_ID);
        //todo

    }

    @Override
    public void changeStatus(UUID orderId, ChangeStatusDto changeStatusDto) {
        var orderEntity = orderRepository.findById(orderId).orElseThrow(
                () -> new NoSuchElementException(ORDER_NOT_FOUND.getMessage())
        );

        orderEntity.setStatus(changeStatusDto.getStatus());
        orderRepository.save(orderEntity);

        log.info("СТАТУС ЗАКАЗА ИЗМЕНЕН!!!!!!!!!! {}", orderEntity.getId());
    }

    @Override
    public GetOrderDto getOrder(UUID orderId) {
        var customerId = request.getHeader(HEADER_CUSTOMER_ID);
        if (customerId == null) throw new BusinessException();

        var orderEntity = orderRepository.findById(orderId).orElseThrow(
                () -> new NoSuchElementException(ORDER_NOT_FOUND.getMessage())
        );

        if (!orderEntity.getCustomer().getId().toString().equals(customerId)) throw new BusinessException();

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

            var orderedProductId = new OrderedProductId(orderEntity.getId(), product.getId());

            if (isUpdate) {
                orderedProductRepository.findById(orderedProductId).ifPresent(orderedProductEntity -> {
                    productEntity.setQty(productEntity.getQty().add(orderedProductEntity.getQuantity()));
                });
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