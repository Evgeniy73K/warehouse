package org.mediasoft.warehouse.error.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CommonErrorEnum {
    ORDER_NOT_FOUND("Заказ не найден"),
    IMPOSSIBLE_UPDATE_ORDER("Невозможно обновить заказ"),
    IMPOSSIBLE_DELETE_ORDER("Невозможно удалить заказ");

    private final String message;
}
