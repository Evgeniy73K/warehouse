package org.mediasoft.warehouse.db.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatusEnum {
    CREATED,
    CONFIRMED,
    CANCELLED,
    DONE,
    REJECTED;
}
