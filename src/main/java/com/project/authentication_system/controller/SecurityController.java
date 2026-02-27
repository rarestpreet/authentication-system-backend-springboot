package com.project.authentication_system.controller;

import com.project.authentication_system.dto.request.LoginRequestDTO;
import com.project.authentication_system.dto.request.RegisterRequestDTO;
import com.project.authentication_system.dto.request.ResetPassRequestDTO;
import com.project.authentication_system.dto.request.VerifyEmailRequestDTO;
import com.project.authentication_system.dto.response.UserResponseDTO;
import com.project.authentication_system.exception.InvalidOtpException;
import com.project.authentication_system.service.SecurityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SecurityController {

    private final SecurityService securityService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        UserResponseDTO userResponseDTO = securityService.registerUser(registerRequestDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userResponseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        String token = securityService.authenticateUser(loginRequestDTO);

        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(60 * 60 * 1000)
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(token);
    }

    @GetMapping("/is-authenticated")
    public ResponseEntity<?> isAuthenticated(Authentication authentication) {
        if(authentication == null ||
                authentication instanceof AnonymousAuthenticationToken) {
            return ResponseEntity.status(HttpStatus.OK).body(true);
        }
        return ResponseEntity.status(HttpStatus.OK).body(authentication.getName()!=null);
    }

    @GetMapping("/send-reset-otp")
    public ResponseEntity<?> sendResetOtp(@RequestParam String email) {
        securityService.sendPasswordResetOtp(email);
        return ResponseEntity.status(HttpStatus.OK).body("Reset OTP has been sent");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPassRequestDTO resetPassRequestDTO) throws InvalidOtpException {
        securityService.resetUserPassword(resetPassRequestDTO.getEmail(),
                resetPassRequestDTO.getOtp(),
                resetPassRequestDTO.getPassword()
        );

        return ResponseEntity.status(HttpStatus.OK).body("Password changed successfully");
    }

    @GetMapping("/send-verify-otp")
    public ResponseEntity<?> sendVerifyOtp(Authentication authentication) {
        securityService.sendEmailVerifyOtp(authentication.getName());
        return ResponseEntity.status(HttpStatus.OK).body("Email verification OTP has been sent");
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@Valid @RequestBody VerifyEmailRequestDTO verifyEmailRequestDTO) throws InvalidOtpException {
        securityService.verifyUserEmail(
                verifyEmailRequestDTO.getEmail(),
                verifyEmailRequestDTO.getOtp()
        );
        return ResponseEntity.status(HttpStatus.OK).body("Email verified successfully");
    }
}
