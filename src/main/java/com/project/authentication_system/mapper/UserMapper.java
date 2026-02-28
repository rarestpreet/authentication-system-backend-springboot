package com.project.authentication_system.mapper;

import com.project.authentication_system.dto.request.RegisterRequestDTO;
import com.project.authentication_system.dto.response.UserResponseDTO;
import com.project.authentication_system.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public static UserResponseDTO toDTO(User user) {
        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .isAccountVerified(user.getIsAccountVerified())
                .build();
    }

    public static User toEntity(RegisterRequestDTO registerRequestDTO) {
        return User.builder()
                .userId(UUID.randomUUID().toString())
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
}
