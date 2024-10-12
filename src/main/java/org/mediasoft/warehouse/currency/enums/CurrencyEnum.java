package org.mediasoft.warehouse.currency.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CurrencyEnum {
    USD("USD"),
    EUR("EUR"),
    CNY("CNY"),
    RUB("RUB");

    private final String code;

    @JsonCreator
    public static CurrencyEnum fromValue(String value) {
        if (value == null) value = "RUB";
        for (CurrencyEnum currencyEnum : CurrencyEnum.values()) {
            if (currencyEnum.code.equals(value) || currencyEnum.name().equals(value)) {
                return currencyEnum;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }
}
