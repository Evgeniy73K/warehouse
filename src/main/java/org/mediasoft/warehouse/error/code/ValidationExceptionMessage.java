package org.mediasoft.warehouse.error.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ValidationExceptionMessage {
    USER_NOT_FOUND("Пользователь не найден id:"),
    USER_NOT_AVAILABLE("Пользователь заблокирован id:"),
    PRODUCT_NOT_FOUND("Товары не найдены id:"),
    PRODUCT_NOT_AVAILABLE("Товары не доступны к заказу id:"),
    NOT_ENOUGH_QTY("Недостаточно товара id, доступное количество");

    private final String message;
}
