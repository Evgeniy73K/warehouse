package org.mediasoft.warehouse.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ProductValidationException extends RuntimeException {
    private final List<UUID> unAvailableProducts;
    private final List<UUID> notFoundProducts;
    private final Map<UUID, BigDecimal> notEnoughProductMap;
}
