package org.mediasoft.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class ResponseProductDto {
    private String name;
    private UUID article;
    private String category;
    private BigDecimal price;
    private BigDecimal qty;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
