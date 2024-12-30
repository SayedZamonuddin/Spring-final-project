package org.ucentralasia.NovelNest.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.ucentralasia.NovelNest.model.user.User;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Value("${app.base-url}")
    private String baseUrl;   // for example http://localhost:8080

    @Autowired
    private JavaMailSender mailsender;

    public void sendVerificationEmail(String recipientEmail, String token) {
        String subject = "Verification Email";
        String verificationUrl = baseUrl + "/auth/verify?token=" + token;
        String message = "Please verify your email by clicking the following link: " + verificationUrl;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientEmail);
        email.setSubject(subject);
        email.setText(message);
        email.setFrom("nazarbekovnavruz@gmail.com");

        mailsender.send(email);
    }

    public void sendPasswordResetEmail(String recipientEmail, String token) {
//        if the email has been sent already, display a message like "email sent, resent in 30minutes whatever"
        // only send if the user is enabled
        String subject = "Password Reset Email";
        String resetUrl = "http://localhost:5173/reset-password?token=" + token;
//        String resetUrl = "http://localhost:3000/reset-password?token=" + token;
        String message = "You have requested to reset your password. Please click the following link to reset it:  " + resetUrl;

        SimpleMailMessage email = new SimpleMailMessage();

        email.setTo(recipientEmail);
        email.setSubject(subject);
        email.setText(message);
        email.setFrom("nazarbekovnavruz@gmail.com");

        try{
            mailsender.send(email);
            logger.info("Password reset email sent to {}", recipientEmail);
        } catch (Exception e){
            logger.error("Failed to send password reset email to {}: {}", recipientEmail, e.getMessage());
            throw e;
        }
    }

}
