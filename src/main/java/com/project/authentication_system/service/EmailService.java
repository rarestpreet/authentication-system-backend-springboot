package com.project.authentication_system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.properties.mail.smtp.from")
    private String senderMail;

    @Async
    public void sendWelcomeMail(String receiverMail, String name) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(receiverMail);
        mailMessage.setFrom(senderMail);
        mailMessage.setSubject("Welcome to our platform");
        mailMessage.setText("Hello, " + name + ", \n\nThanks for registering with us!");

        mailSender.send(mailMessage);
    }

    @Async
    public void sendPassRestOtpEmail(String receiverMail, String otp) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(receiverMail);
        mailMessage.setFrom(senderMail);
        mailMessage.setSubject("Password Reset OTP");
        mailMessage.setText(otp+" is your otp for resetting your password.");

        mailSender.send(mailMessage);
    }

    @Async
    public void sendVerifyOtpEmail(String receiverMail, String otp) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(receiverMail);
        mailMessage.setFrom(senderMail);
        mailMessage.setSubject("Email verification OTP");
        mailMessage.setText(otp+" is your otp for verifying your email.");

        mailSender.send(mailMessage);
    }
}
