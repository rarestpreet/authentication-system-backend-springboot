package com.project.authentication_system.controller;

import com.project.authentication_system.dto.request.TodoRequestDTO;
import com.project.authentication_system.dto.response.TodoResponseDTO;
import com.project.authentication_system.entity.CustomUserDetails;
import com.project.authentication_system.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
@PreAuthorize("isFullyAuthenticated()")
@Tag(name = "Todo APIs")
public class TodoController {

    private final TodoService todoService;

    @Operation(summary = "Create task", description = "Creates a new task for the authenticated user.")
    @PostMapping("")
    public ResponseEntity<?> createTodo(@Valid @RequestBody TodoRequestDTO dto,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        TodoResponseDTO response = todoService.createTodo(dto, userDetails.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body("Task created successfully");
    }

    @Operation(summary = "Get user tasks", description = "Retrieves all tasks for the authenticated user.")
    @GetMapping("")
    public ResponseEntity<?> getTodos(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(todoService.getAllTodosForUser(userDetails.getUsername()));
    }

    @Operation(summary = "Update task", description = "Updates an existing task by ID for the authenticated user.")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTodo(@PathVariable UUID id, @Valid @RequestBody TodoRequestDTO dto,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        todoService.updateTodo(id, dto, userDetails);

        return ResponseEntity.ok("Todo updated successfully");
    }

    @Operation(summary = "Delete task", description = "Deletes a task by ID for the authenticated user.")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable UUID id,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        todoService.deleteTodo(id, userDetails);

        return ResponseEntity.ok("Todo deleted successfully");
    }

    @Operation(summary = "Get all tasks (Admin)", description = "Retrieves all tasks across all users. Requires ADMIN role.")
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllTasks() {
        return ResponseEntity.ok(todoService.getAllTodosAdmin());
    }
}
