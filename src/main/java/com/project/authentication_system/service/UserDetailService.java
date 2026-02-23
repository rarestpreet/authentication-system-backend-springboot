package com.project.authentication_system.service;

import com.project.authentication_system.entity.CustomUserDetails;
import com.project.authentication_system.entity.User;
import com.project.authentication_system.exception.UserNotFoundException;
import com.project.authentication_system.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public @NullMarked class UserDetailService implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("No user found with the email"));

        return new CustomUserDetails(user);
    }
}
