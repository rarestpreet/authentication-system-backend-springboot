package com.project.authentication_system.service;

import com.project.authentication_system.dto.request.TodoRequestDTO;
import com.project.authentication_system.dto.response.TodoResponseDTO;
import com.project.authentication_system.entity.CustomUserDetails;
import com.project.authentication_system.entity.enums.Role;
import com.project.authentication_system.entity.Todo;
import com.project.authentication_system.entity.User;
import com.project.authentication_system.exception.InvalidOperationException;
import com.project.authentication_system.mapper.TodoMapper;
import com.project.authentication_system.repository.TodoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserService userService;

    @Transactional
    public TodoResponseDTO createTodo(TodoRequestDTO todoRequestDTO, String email) {
        User user = userService.getUserDetails(email);
        Todo todo = TodoMapper.toEntity(todoRequestDTO, user);
        todo = todoRepository.save(todo);
        return TodoMapper.toDTO(todo);
    }

    public List<TodoResponseDTO> getAllTodosForUser(String email) {
        User user = userService.getUserDetails(email);

        if (user.getRoles().contains(Role.ADMIN)) {
            return todoRepository.findAll().stream()
                    .map(TodoMapper::toDTO)
                    .collect(Collectors.toList());
        }
        return todoRepository.findAllByUser_Id(user.getId()).stream()
                .map(TodoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateTodo(UUID todoId, TodoRequestDTO requestDTO, CustomUserDetails userDetails) {
        User user = userService.getUserDetails(userDetails.getUsername());
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new RuntimeException("Todo not found"));

        if (!todo.getUser().getId().toString().equals(user.getId().toString()) && !user.getRoles().contains(Role.ADMIN)) {
            throw new InvalidOperationException("Not allowed to perform update");
        }

        if(user.getRoles().contains(Role.ADMIN)) {
            log.debug("admin {} is updating task {}", user.getUsername(), todoId);
        }

        todo.setTask(requestDTO.getTask());
        todoRepository.save(todo);
    }

    @Transactional
    public void deleteTodo(UUID todoId, CustomUserDetails userDetails) {
        User user = userService.getUserDetails(userDetails.getUsername());
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new RuntimeException("Todo not found"));

        if (!todo.getUser().getId().toString().equals(user.getId().toString()) && !user.getRoles().contains(Role.ADMIN)) {
            throw new InvalidOperationException("Not allowed to perform delete");
        }

        if(user.getRoles().contains(Role.ADMIN)) {
            log.debug("admin {} is deleting task {}", user.getUsername(), todoId);
        }

        todoRepository.delete(todo);
    }

    public List<TodoResponseDTO> getAllTodosAdmin() {
        return todoRepository.findAll().stream()
                .map(TodoMapper::toDTO)
                .collect(Collectors.toList());
    }
}
