package com.project.authentication_system.service;

import com.project.authentication_system.dto.request.LoginRequestDTO;
import com.project.authentication_system.dto.request.RegisterRequestDTO;
import com.project.authentication_system.dto.response.UserResponseDTO;
import com.project.authentication_system.entity.User;
import com.project.authentication_system.exception.EmailAlreadyExistException;
import com.project.authentication_system.mapper.UserMapper;
import com.project.authentication_system.repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepo userRepo;
    private final JWTService jwtService;
    private final EmailService emailService;

    @Transactional
    public UserResponseDTO registerUser(RegisterRequestDTO registerRequestDTO) {
        if(userRepo.existsByEmail(registerRequestDTO.getEmail())) {
            throw new EmailAlreadyExistException("Email already exists");
        }

        User newUser = UserMapper.toEntity(registerRequestDTO);
        newUser.setPassword(
                passwordEncoder.encode(registerRequestDTO.getPassword())
        );

        User savedUser = userRepo.save(newUser);
        emailService.sendMail(newUser.getEmail(), newUser.getUsername());

        return UserMapper.toDTO(savedUser);
    }

    public String authenticateUser(LoginRequestDTO loginRequestDTO) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getEmail(), loginRequestDTO.getPassword()
                )
        );

        return jwtService.generateToken(loginRequestDTO.getEmail());
    }
}
