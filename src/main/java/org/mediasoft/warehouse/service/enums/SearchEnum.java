package org.mediasoft.warehouse.service.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SearchEnum {
    EQUAL("="),
    GRATER_THAN_OR_EQ(">="),
    LESS_THAN_OR_EQ("<="),
    LIKE("~");

    private final String value;

    public static SearchEnum fromValue(String value) {
        for (SearchEnum searchEnum : SearchEnum.values()) {
            if (searchEnum.value.equals(value)) {
                return searchEnum;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }
}