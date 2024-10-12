package org.mediasoft.warehouse.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.mediasoft.warehouse.currency.enums.CurrencyEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ResponseProductDto {
    private String name;
    private UUID article;
    private String category;
    private BigDecimal price;
    private BigDecimal qty;
    private LocalDateTime insertedAt;
    private LocalDateTime lastUpdated;
    private CurrencyEnum currency;
}
