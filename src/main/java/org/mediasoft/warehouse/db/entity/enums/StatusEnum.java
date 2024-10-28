package org.mediasoft.warehouse.db.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatusEnum {
    CREATED("CREATED"),
    CONFIRMED("CONFIRMED"),
    CANCELLED("CANCELLED"),
    DONE("DONE"),
    REJECTED("REJECTED"),;

    private final String name;

    @JsonCreator
    public static StatusEnum fromValue(String value) {
        for (StatusEnum statusEnum : StatusEnum.values()) {
            if (statusEnum.name.equals(value) || statusEnum.name().equals(value) ) {
                return statusEnum;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }
}
