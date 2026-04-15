package com.project.authentication_system.service;

import com.project.authentication_system.config.CookieProperties;
import com.project.authentication_system.dto.request.LoginRequestDTO;
import com.project.authentication_system.dto.request.RegisterRequestDTO;
import com.project.authentication_system.dto.response.UserResponseDTO;
import com.project.authentication_system.entity.User;
import com.project.authentication_system.exception.EmailAlreadyExistException;
import com.project.authentication_system.exception.InvalidOtpException;
import com.project.authentication_system.mapper.UserMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final HttpServletRequest httpServletRequest;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final CookieProperties cookieProperties;

    @Transactional
    public void registerUser(RegisterRequestDTO registerRequestDTO) {
        if (userService.userExist(registerRequestDTO)) {
            throw new EmailAlreadyExistException("Email already exists");
        }

        User newUser = UserMapper.toEntity(registerRequestDTO);
        newUser.setPassword(
                passwordEncoder.encode(registerRequestDTO.getPassword()));

        userService.saveUserDetails(newUser);
        emailService.sendWelcomeMail(newUser.getEmail(), newUser.getUsername());
    }

    public ResponseCookie authenticateUser(LoginRequestDTO loginRequestDTO) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getEmail(), loginRequestDTO.getPassword()));

        String token = jwtService.generateToken(loginRequestDTO.getEmail());

        return ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(cookieProperties.isSecure())
                .path("/")
                .maxAge(60 * 15)
                .sameSite(cookieProperties.getSameSite())
                .build();
    }

    public void sendPasswordResetOtp(String email) {
        User currentUser = userService.getUserDetails(email);

        String otp = generateOtp();
        long expirationTime = System.currentTimeMillis() + 10 * 60 * 1000;

        currentUser.setResetOtp(passwordEncoder.encode(otp));
        currentUser.setResetOtpExpireAt(expirationTime);
        userService.saveUserDetails(currentUser);

        try {
            emailService.sendPassResetOtpEmail(email, otp);
        } catch (Exception e) {
            throw new RuntimeException("Unable to send email. Try again later.");
        }
    }

    public String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public ResponseCookie resetUserPassword(String email, String otp, String password) throws InvalidOtpException {
        User currentUser = userService.getUserDetails(email);

        if (currentUser.getResetOtp() == null ||
                !passwordEncoder.matches(otp, currentUser.getResetOtp())) {
            throw new InvalidOtpException("Please provide the Otp sent on your mail.");
        }

        if (currentUser.getResetOtpExpireAt() < System.currentTimeMillis()) {
            throw new InvalidOtpException("Otp expired, please create a new one.");
        }

        currentUser.setPassword(passwordEncoder.encode(password));
        currentUser.setResetOtpExpireAt(null);
        currentUser.setResetOtp(null);

        userService.saveUserDetails(currentUser);

        return clearedCookie();
    }

    public void sendEmailVerifyOtp(String email) {
        User currentUser = userService.getUserDetails(email);

        String otp = generateOtp();
        long expirationTime = System.currentTimeMillis() + 5 * 60 * 60 * 1000;

        currentUser.setVerifyOtp(passwordEncoder.encode(otp));
        currentUser.setVerifyOtpExpireAt(expirationTime);

        userService.saveUserDetails(currentUser);

        try {
            emailService.sendVerifyOtpEmail(email, otp);
        } catch (Exception e) {
            throw new RuntimeException("Unable to send email. Try again later.");
        }
    }

    public void verifyUserEmail(String email, String otp) throws InvalidOtpException {
        User currentUser = userService.getUserDetails(email);

        if (currentUser.getVerifyOtp() == null ||
                !passwordEncoder.matches(otp, currentUser.getVerifyOtp())) {
            throw new InvalidOtpException("Please provide the Otp sent on your mail.");
        }

        if (currentUser.getVerifyOtpExpireAt() < System.currentTimeMillis()) {
            throw new InvalidOtpException("Otp expired, please create a new one.");
        }

        currentUser.setIsAccountVerified(true);
        currentUser.setVerifyOtpExpireAt(null);
        currentUser.setVerifyOtp(null);

        userService.saveUserDetails(currentUser);
    }

    public ResponseCookie terminateSession() {
        HttpSession session = httpServletRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();

        return clearedCookie();
    }

    public ResponseCookie clearedCookie() {
        return ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(cookieProperties.isSecure())
                .path("/")
                .sameSite(cookieProperties.getSameSite())
                .maxAge(0)
                .build();
    }
}
