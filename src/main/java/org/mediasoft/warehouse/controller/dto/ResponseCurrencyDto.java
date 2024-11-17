package org.mediasoft.warehouse.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class ResponseCurrencyDto {
    @JsonProperty("CNY")
    private BigDecimal CNY;
    @JsonProperty("USD")
    private BigDecimal USD;
    @JsonProperty("EUR")
    private BigDecimal EUR;
}
