package com.project.authentication_system.controller;

import com.project.authentication_system.dto.request.LoginRequestDTO;
import com.project.authentication_system.dto.request.RegisterRequestDTO;
import com.project.authentication_system.dto.request.ResetPassRequestDTO;
import com.project.authentication_system.dto.request.VerifyEmailRequestDTO;
import com.project.authentication_system.entity.CustomUserDetails;
import com.project.authentication_system.exception.InvalidOtpException;
import com.project.authentication_system.service.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Security APIs")
public class SecurityController {

    private final SecurityService securityService;

    @Operation(summary = "Register a new user", description = "Creates a new user account with the provided username, email, and password.")
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        securityService.registerUser(registerRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @Operation(summary = "Login an existing user", description = "Authenticates a user and returns a session cookie.")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        ResponseCookie cookie = securityService.authenticateUser(loginRequestDTO);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("User logged in successfully");
    }

    @Operation(summary = "Send password reset OTP", description = "Sends a One-Time Password to the user's email for password reset.")
    @PostMapping("/send-reset-otp")
    public ResponseEntity<?> sendResetOtp(@RequestParam String email) {
        securityService.sendPasswordResetOtp(email);

        return ResponseEntity.status(HttpStatus.OK).body("Reset OTP has been sent");
    }

    @Operation(summary = "Reset user password", description = "Resets the user's password using the provided email, OTP, and new password.")
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPassRequestDTO resetPassRequestDTO)
            throws InvalidOtpException {
        ResponseCookie cookie = securityService.resetUserPassword(resetPassRequestDTO.getEmail(),
                resetPassRequestDTO.getOtp(),
                resetPassRequestDTO.getPassword());

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Password changed successfully");
    }

    @Operation(summary = "Send email verification OTP", description = "Sends a One-Time Password to the authenticated user's email for verification.")
    @GetMapping("/send-verify-otp")
    @PreAuthorize("isFullyAuthenticated()")
    public ResponseEntity<?> sendVerifyOtp(@AuthenticationPrincipal CustomUserDetails userDetails) {
        securityService.sendEmailVerifyOtp(userDetails.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body("Email verification OTP has been sent");
    }

    @Operation(summary = "Verify user email", description = "Verifies the authenticated user's email using the provided OTP.")
    @PostMapping("/verify-email")
    @PreAuthorize("isFullyAuthenticated()")
    public ResponseEntity<?> verifyEmail(@Valid @RequestBody VerifyEmailRequestDTO verifyEmailRequestDTO,
                                         @AuthenticationPrincipal CustomUserDetails userDetails)
            throws InvalidOtpException {
        securityService.verifyUserEmail(userDetails.getUsername(), verifyEmailRequestDTO.getOtp());

        return ResponseEntity.status(HttpStatus.OK).body("Email verified successfully");
    }

    @Operation(summary = "Logout user", description = "Terminates the user's session and clears the session cookie.")
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        ResponseCookie cookie = securityService.terminateSession();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Logged out");
    }
}
