package org.mediasoft.warehouse.error.exception;

import lombok.Getter;

import static org.mediasoft.warehouse.error.code.ValidationExceptionMessage.USER_NOT_AVAILABLE;

@Getter
public class UserNotAvailableException extends RuntimeException {
    private final Long userId;

    public UserNotAvailableException(Long userId) {
        super(USER_NOT_AVAILABLE.getMessage());
        this.userId = userId;
    }
}
