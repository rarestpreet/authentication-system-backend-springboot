package com.project.authentication_system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class ResetPassRequestDTO {

    @Schema(description = "Email address associated with the account to reset password", example = "user@example.com")
    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Email must be valid")
    private String email;

    @Schema(description = "One-Time Password (OTP) received for verification", example = "123456")
    @NotBlank(message = "Otp is required for verification")
    private String otp;

    @Schema(description = "New password to set", example = "NewStrongPass!1")
    @NotBlank(message = "New password is required")
    private String password;
}
