package com.project.authentication_system.mapper;

import com.project.authentication_system.dto.request.RegisterRequestDTO;
import com.project.authentication_system.dto.response.AdminUserResponseDTO;
import com.project.authentication_system.dto.response.UserResponseDTO;
import com.project.authentication_system.entity.User;
import com.project.authentication_system.entity.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public static UserResponseDTO toDTO(User user) {
        return UserResponseDTO.builder()
                .userId(user.getId().toString())
                .username(user.getUsername())
                .isAccountVerified(user.getIsAccountVerified())
                .role(user.getRoles().contains(Role.ADMIN) ? "ADMIN" : "USER")
                .build();
    }

    public static User toEntity(RegisterRequestDTO registerRequestDTO) {
        return User.builder()
                .username(registerRequestDTO.getUsername())
                .password(registerRequestDTO.getPassword())
                .email(registerRequestDTO.getEmail())
                .verifyOtp(null)
                .isAccountVerified(false)
                .verifyOtpExpireAt(0L)
                .resetOtp(null)
                .resetOtpExpireAt(0L)
                .build();
    }

    public static AdminUserResponseDTO toAdminDTO(User user) {
        return AdminUserResponseDTO.builder()
                .userId(user.getId().toString())
                .username(user.getUsername())
                .email(user.getEmail())
                .isAccountVerified(user.getIsAccountVerified())
                .role(user.getRoles().contains(Role.ADMIN) ? "ADMIN" : "USER")
                .createdAt(user.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                .build();
    }
}
