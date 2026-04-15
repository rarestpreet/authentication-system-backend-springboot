package com.project.authentication_system.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.properties.mail.smtp.from")
    private String senderMail;

    @Async
    public void sendWelcomeMail(String receiverMail, String name) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setTo(receiverMail);
            mailMessage.setFrom(senderMail);
            mailMessage.setSubject("Welcome to our platform");
            mailMessage.setText("Hello, " + name + ", \n\nThanks for registering with us!");

            mailSender.send(mailMessage);
            log.debug("Registration mail sent successfully");
        } catch (Exception e) {
            log.error("Operation failed: sendWelcomeMail\n Caused by: {}", e.getMessage());
        }
    }

    @Async
    public void sendPassResetOtpEmail(String receiverMail, String otp) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setTo(receiverMail);
            mailMessage.setFrom(senderMail);
            mailMessage.setSubject("Password Reset OTP");
            mailMessage.setText(otp + " is your otp for resetting your password.");

            mailSender.send(mailMessage);
            log.debug("Password reset mail sent successfully");
        } catch (Exception e) {
            log.error("Operation failed: sendPassResetOtpEmail\n Caused by: {}", e.getMessage());
        }
    }

    @Async
    public void sendVerifyOtpEmail(String receiverMail, String otp) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setTo(receiverMail);
            mailMessage.setFrom(senderMail);
            mailMessage.setSubject("Email verification OTP");
            mailMessage.setText(otp + " is your otp for verifying your email.");

            mailSender.send(mailMessage);
            log.debug("Account verification mail sent successfully");
        } catch (Exception e) {
            log.error("Operation failed: sendVerifyOtpEmail\n Caused by: {}", e.getMessage());
        }
    }
}
