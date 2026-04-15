package com.project.authentication_system.mapper;

import com.project.authentication_system.dto.request.TodoRequestDTO;
import com.project.authentication_system.dto.response.TodoResponseDTO;
import com.project.authentication_system.entity.Todo;
import com.project.authentication_system.entity.User;

import java.time.format.DateTimeFormatter;

public class TodoMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static Todo toEntity(TodoRequestDTO dto, User user) {
        return Todo.builder()
                .user(user)
                .task(dto.getTask())
                .build();
    }

    public static TodoResponseDTO toDTO(Todo entity) {
        return TodoResponseDTO.builder()
                .id(entity.getId())
                .task(entity.getTask())
                .updatedAt(entity.getCreatedAt().format(formatter))
                .build();
    }
}
