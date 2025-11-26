package com.spring.boot.job.tracker.app.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendWelcomeEmail(String to, String username) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject("Welcome back, " + username + "!");
        helper.setText("""
                <h2>Welcome back to Job Tracker 👋</h2>
                <p>Hello <b>%s</b>,</p>
                <p>We’re glad to see you again! You can continue tracking your applications and progress easily.</p>
                <p>– The Job Tracker Team</p>
                """.formatted(username), true);
        log.info("Sending Welcome Email to {}",to);
        mailSender.send(message);
    }


    @Async
    public void sendEmailOTP(String to, String username, int otp) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false); 

        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject("Your Job Tracker Password Reset OTP");

        String textContent = """
                Hello %s,

                You requested to reset your password on Job Tracker.

                Your One-Time Password (OTP) is: %s

                This OTP is valid for 5 minutes. Please do not share it with anyone.

                If you did not request this, please consider changing your password immediately.

                - Job Tracker Support Team
                """.formatted(username, otp);

        helper.setText(textContent, false); 

        log.info("Sending OTP Email to {}", to);
        mailSender.send(message);
    }

}
