package org.mediasoft.warehouse.error.exception;

import lombok.Getter;

import static org.mediasoft.warehouse.error.code.ValidationExceptionMessage.USER_NOT_FOUND;

@Getter
public class UserNotFoundException extends RuntimeException {
    private final Long userId;

    public UserNotFoundException(Long userId) {
        super(USER_NOT_FOUND.getMessage());
        this.userId = userId;
    }
}
