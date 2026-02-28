package com.project.authentication_system.service;

import com.project.authentication_system.dto.request.RegisterRequestDTO;
import com.project.authentication_system.dto.response.UserResponseDTO;
import com.project.authentication_system.entity.User;
import com.project.authentication_system.exception.UserNotFoundException;
import com.project.authentication_system.mapper.UserMapper;
import com.project.authentication_system.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    public UserResponseDTO getUserProfile(String email) {
        User currentUser = userRepo
                .findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("No user found with the email")
                );
        return UserMapper.toDTO(currentUser);
    }

    public User getUserDetails(String email) {
        return userRepo
                .findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("No user found with the email")
                );
    }

    public void saveUserDetails(User currentUser) {
        userRepo.save(currentUser);
    }

    public boolean userExist(RegisterRequestDTO registerRequestDTO) {
        return userRepo.existsByEmail(registerRequestDTO.getEmail());
    }
}
