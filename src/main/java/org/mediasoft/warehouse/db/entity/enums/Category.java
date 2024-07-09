package org.mediasoft.warehouse.db.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    FRUITS("fruits"),
    VEGETABLES("vegetables");

    private final String name;
}