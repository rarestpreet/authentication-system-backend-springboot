package com.project.authentication_system.controller;

import com.project.authentication_system.dto.response.AdminUserResponseDTO;
import com.project.authentication_system.entity.CustomUserDetails;
import com.project.authentication_system.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
@PreAuthorize("isFullyAuthenticated()")
@Tag(name = "User APIs")
@NullMarked
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get user profile", description = "Retrieves the profile information for the authenticated user.")
    @GetMapping("")
    public ResponseEntity<?> getUserProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getUserProfile(userDetails.getUsername()));
    }

    @Operation(summary = "Get all users (Admin)", description = "Retrieves a list of all users. Requires ADMIN role.")
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AdminUserResponseDTO>> getAllUsers(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(userService.getAllUsers(userDetails));
    }
}
