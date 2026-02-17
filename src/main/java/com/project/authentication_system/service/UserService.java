package com.project.authentication_system.service;

import com.project.authentication_system.dto.request.UserRequestDTO;
import com.project.authentication_system.dto.response.UserResponseDTO;
import com.project.authentication_system.entity.User;
import com.project.authentication_system.mapper.UserMapper;
import com.project.authentication_system.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        User newUser = userRepo.save(UserMapper.toEntity(userRequestDTO));
        return UserMapper.toDTO(newUser);
    }

}
