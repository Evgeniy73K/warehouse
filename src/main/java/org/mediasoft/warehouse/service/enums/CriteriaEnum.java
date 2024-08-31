package org.mediasoft.warehouse.service.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CriteriaEnum {
    EQUAL("="),
    GRATER_THAN_OR_EQ(">="),
    LESS_THAN_OR_EQ("<="),
    LIKE("~");

    private final String code;

    @JsonCreator
    public static CriteriaEnum fromValue(String value) {
        for (CriteriaEnum criteriaEnum : CriteriaEnum.values()) {
            if (criteriaEnum.code.equals(value) || criteriaEnum.name().equals(value) ) {
                return criteriaEnum;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }
}