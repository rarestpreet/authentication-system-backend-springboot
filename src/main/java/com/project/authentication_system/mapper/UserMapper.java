package com.project.authentication_system.mapper;

import com.project.authentication_system.dto.request.UserRequestDTO;
import com.project.authentication_system.dto.response.UserResponseDTO;
import com.project.authentication_system.entity.User;

import java.util.UUID;

public class UserMapper {

    public static UserResponseDTO toDTO(User user) {
        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .password(user.getPassword())
                .isAccountVerified(user.getIsAccountVerified())
                .build();
    }

    public static User toEntity(UserRequestDTO userRequestDTO) {
        return User.builder()
                .userId(UUID.randomUUID().toString())
                .username(userRequestDTO.getUsername())
                .password(userRequestDTO.getPassword())
                .email(userRequestDTO.getEmail())
                .verifyOtp(null)
                .isAccountVerified(false)
                .verifyOtpExpireAt(0L)
                .resetOtp(null)
                .resetOtpExpireAt(0L)
                .build();

    }
}
