package org.mediasoft.warehouse.controller.order.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class OrderInfoDto {
    private UUID id;
    private CustomerInfo customer;
    private String status;
    private String deliveryAddress;
    private BigDecimal quantity;

    @Data
    @Builder
    public static class CustomerInfo {
        private Long id;
        private String accountNumber;
        private String inn;
        private String email;
    }
}



