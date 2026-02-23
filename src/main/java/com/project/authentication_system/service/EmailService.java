package com.project.authentication_system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.properties.mail.smtp.from")
    private String senderMail;

    public void sendMail(String to, String name) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(to);
        mailMessage.setSubject("Welcome to our platform");
        mailMessage.setText("Hello, " + name + ", \n\nThanks for registering with us!");
        mailMessage.setFrom(senderMail);

        mailSender.send(mailMessage);
    }

    public void sendPassRestOtpEmail(String to, String otp) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(to);
        mailMessage.setFrom(senderMail);
        mailMessage.setSubject("Password Reset OTP");
        mailMessage.setText(otp+" is your otp for resetting your password.");
        mailMessage.setFrom(senderMail);

        mailSender.send(mailMessage);
    }
}
