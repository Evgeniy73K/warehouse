package org.mediasoft.warehouse.error.exception;

import lombok.Getter;

import java.util.UUID;
@Getter
public class SkuIsExistException extends RuntimeException {
    private UUID productId;
    public SkuIsExistException(String message, UUID productId) {
        super(message);
        this.productId = productId;
    }
}
