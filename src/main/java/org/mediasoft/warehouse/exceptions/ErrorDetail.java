package org.mediasoft.warehouse.exceptions;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorDetail {
    private String exceptionName;
    private String message;
    private LocalDateTime time;
    private Class<?> clazz;
}
