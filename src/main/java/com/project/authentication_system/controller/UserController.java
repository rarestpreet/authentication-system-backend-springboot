package com.project.authentication_system.controller;

import com.project.authentication_system.exception.UserNotLoggedException;
import com.project.authentication_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(Authentication authentication) throws UserNotLoggedException {
        if(authentication==null ||
                authentication instanceof AnonymousAuthenticationToken){
            throw new UserNotLoggedException("No logged in user found");
        }
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(userService.getUserProfile(authentication.getName()));
    }
}
