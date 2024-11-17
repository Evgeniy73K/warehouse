package org.mediasoft.warehouse.service.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class UpdateProductDto {
    private String name;
    private UUID id;
    private UUID article;
    private String category;
    private String dictionary;
    private BigDecimal price;
    private BigDecimal qty;
}
