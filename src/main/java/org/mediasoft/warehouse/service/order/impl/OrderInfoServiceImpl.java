package org.mediasoft.warehouse.service.order.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mediasoft.warehouse.controller.order.dto.OrderInfoDto;
import org.mediasoft.warehouse.db.entity.OrderedProductEntity;
import org.mediasoft.warehouse.db.repository.OrderedProductRepository;
import org.mediasoft.warehouse.service.order.OrderInfoService;
import org.mediasoft.warehouse.service.order.client.AccountServiceApiClient;
import org.mediasoft.warehouse.service.order.client.CrmServiceApiClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.mediasoft.warehouse.db.entity.enums.OrderStatusEnum.CONFIRMED;
import static org.mediasoft.warehouse.db.entity.enums.OrderStatusEnum.CREATED;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderInfoServiceImpl implements OrderInfoService {
    private final OrderedProductRepository orderedProductRepository;
    private final AccountServiceApiClient accountServiceApiClient;
    private final CrmServiceApiClient crmServiceApiClient;

    public HashMap<UUID, List<OrderInfoDto>> getOrderInfo(UUID productId) {
        List<OrderInfoDto> orderInfoDtoList = new ArrayList<>();
        Set<String> loginSetList = new HashSet<>();
        HashMap<UUID, List<OrderInfoDto>> orderInfoMap = new HashMap<>();

        final Map<UUID, OrderedProductEntity> orderedProductEntitiesMap = orderedProductRepository.findAllByProductId(productId)
            .stream()
            .filter(p -> p.getId().getOrderId().getStatus().equals(CREATED) || equals(CONFIRMED))
            .collect(Collectors.toMap(p -> p.getId().getOrderId().getId(), Function.identity()));

        orderedProductEntitiesMap.forEach((key, value) -> loginSetList.add(value.getId().getOrderId().getCustomer().getLogin()));

        List<Map<String, String>> accountList;
        List<Map<String, String>> innList;

        var accountListFeature = accountServiceApiClient.getLogins(loginSetList);
        var accountInnFeature = crmServiceApiClient.getInnList(loginSetList);
        try {
            CompletableFuture.allOf(accountListFeature, accountInnFeature).get(15, TimeUnit.SECONDS);
            accountList = accountListFeature.get();
            innList = accountInnFeature.get();
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }

        if (accountList.isEmpty() || innList.isEmpty()) {
            throw new RuntimeException("accountList or innlist is empty");
        }
        orderedProductEntitiesMap.forEach((key, value) -> {
            var customer = value.getId().getOrderId().getCustomer();
            var order = value.getId().getOrderId();

            OrderInfoDto.CustomerInfo customerInfo = OrderInfoDto.CustomerInfo.builder()
                .id(customer.getId())
                .accountNumber(accountList.stream()
                    .filter(p -> p.containsKey(customer.getLogin())).toList().get(0).get(customer.getLogin()))
                .inn(innList.stream()
                    .filter(p -> p.containsKey(customer.getLogin())).toList().get(0).get(customer.getLogin()))
                .email(customer.getEmail())
                .build();

            var orderInfoDto = OrderInfoDto.builder()
                .id(key)
                .customer(customerInfo)
                .status(order.getStatus().name())
                .deliveryAddress(order.getDeliveryAddress())
                .quantity(value.getQuantity())
                .build();
            orderInfoDtoList.add(orderInfoDto);
        });

        orderInfoMap.put(productId, orderInfoDtoList);

        return orderInfoMap;
    }
}