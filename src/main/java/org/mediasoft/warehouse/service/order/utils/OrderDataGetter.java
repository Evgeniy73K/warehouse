package org.mediasoft.warehouse.service.order.utils;

import lombok.RequiredArgsConstructor;
import org.mediasoft.warehouse.db.entity.CustomerEntity;
import org.mediasoft.warehouse.db.entity.ProductEntity;
import org.mediasoft.warehouse.db.repository.CustomerRepository;
import org.mediasoft.warehouse.db.repository.ProductRepository;
import org.mediasoft.warehouse.error.exception.ProductValidationException;
import org.mediasoft.warehouse.error.exception.UserNotAvailableException;
import org.mediasoft.warehouse.error.exception.UserNotFoundException;
import org.mediasoft.warehouse.service.order.dto.ProductSummaryDto;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class OrderDataGetter {
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public CustomerEntity getUser(Long userId) {
        return customerRepository.findById(userId)
                .map(customerEntity -> {
                    if (!customerEntity.getIsActive()) {
                        throw new UserNotAvailableException(userId);
                    }
                    return customerEntity;
                })
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    public List<ProductEntity> getProducts(List<ProductSummaryDto> productDtos) {

        List<UUID> productIds = productDtos.stream()
                .map(ProductSummaryDto::getId)
                .toList();

        List<ProductEntity> productEntities = productRepository.findAllById(productIds);

        List<UUID> unavailableProductIds = productEntities.stream()
                .filter(p -> p.getIsAvailable().equals(false))
                .map(ProductEntity::getId)
                .toList();

        List<UUID> notFoundProductIds = productIds.stream()
                .filter(productId -> productEntities.stream()
                        .noneMatch(p -> p.getId().equals(productId)))
                .toList();

        var notEnoughProductsMaps = productEntities.stream() //entity to hashMap
                .filter(ProductEntity::getIsAvailable)
                .flatMap(entity -> productDtos.stream()
                        .filter(dto -> dto.getId().equals(entity.getId()))
                        .map(dto -> entity.getQty().subtract(dto.getQty()).compareTo(BigDecimal.ZERO) < 0
                                ? new AbstractMap.SimpleEntry<>(entity.getId(), entity.getQty())
                                : null)
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


        if (!CollectionUtils.isEmpty(unavailableProductIds) || !CollectionUtils.isEmpty(notEnoughProductsMaps) || !CollectionUtils.isEmpty(notFoundProductIds)) {
            throw new ProductValidationException(unavailableProductIds, notFoundProductIds, notEnoughProductsMaps);

        }

        return productEntities;
    }


}
