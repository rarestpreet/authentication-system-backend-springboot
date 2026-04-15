package com.project.authentication_system.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class TodoResponseDTO {
    @Schema(description = "Unique identifier of the Todo item", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;
    @Schema(description = "The task content", example = "Buy groceries")
    private String task;
    @Schema(description = "Last update timestamp in string format", example = "15-04-2026")
    private String updatedAt;
}