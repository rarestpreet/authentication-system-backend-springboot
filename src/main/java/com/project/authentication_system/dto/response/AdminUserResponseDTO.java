package com.project.authentication_system.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class AdminUserResponseDTO {
    @Schema(description = "Unique identifier of the user", example = "123e4567-e89b-12d3-a456-426614174000")
    private String userId;
    @Schema(description = "User's unique username", example = "johndoe")
    private String username;
    @Schema(description = "User's unique username", example = "johndoe@example.com")
    private String email;
    @Schema(description = "Role assigned to the user", example = "ROLE_USER")
    private String role;
    @Schema(description = "Flag indicating if the user's account is verified", example = "true")
    private Boolean isAccountVerified;
    @Schema(description = "User's unique username", example = "dd-MM-YYYY")
    private String createdAt;
}
