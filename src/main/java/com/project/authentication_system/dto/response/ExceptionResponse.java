package com.project.authentication_system.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class ExceptionResponse {
    private LocalDateTime timestamp;
    private int httpStatus;
    private String error;
    private String message;
    private Map<String, String> fieldErrors;
}
