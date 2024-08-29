package org.mediasoft.warehouse.service.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CriteriaEnum {
    EQUAL("="),
    GRATER_THAN_OR_EQ(">="),
    LESS_THAN_OR_EQ("<="),
    LIKE("~");

    private final String value;

    public static CriteriaEnum fromValue(String value) {
        for (CriteriaEnum criteriaEnum : CriteriaEnum.values()) {
            if (criteriaEnum.value.equals(value)) {
                return criteriaEnum;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }
} //переименовать value в code, поправить цикл, json creator, json value аннотация