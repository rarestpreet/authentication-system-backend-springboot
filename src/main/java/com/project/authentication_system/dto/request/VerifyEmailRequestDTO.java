package com.project.authentication_system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class VerifyEmailRequestDTO {

    @Schema(description = "One-Time Password (OTP) received for email verification", example = "654321")
    @NotBlank(message = "Otp is required for verification")
    private String otp;
}
