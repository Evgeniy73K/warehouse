package org.mediasoft.warehouse.error.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorDetail {
    private String exceptionName;
    private String message;
    private LocalDateTime time;
    @JsonProperty("class")
    private Class<?> clazz;
}
