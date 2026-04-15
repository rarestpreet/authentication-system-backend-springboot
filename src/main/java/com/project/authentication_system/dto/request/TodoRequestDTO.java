package com.project.authentication_system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class TodoRequestDTO {
    @Schema(description = "The task content to be completed", example = "Buy groceries")
    @NotBlank(message = "Task cannot be blank")
    private String task;
}