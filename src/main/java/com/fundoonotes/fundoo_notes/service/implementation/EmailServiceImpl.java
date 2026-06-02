package com.fundoonotes.fundoo_notes.service.implementation;

import com.fundoonotes.fundoo_notes.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendOtpEmail(String toEmail, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Fundoo Notes - Password Reset OTP");
            message.setText(
                    "Hello!\n\n" +
                            "Your OTP for password reset is: " + otp + "\n\n" +
                            "This OTP is valid for 10 minutes only.\n\n" +
                            "If you did not request this, " +
                            "please ignore this email.\n\n" +
                            "Regards,\n" +
                            "Fundoo Notes Team"
            );
            mailSender.send(message);
            log.info("OTP email sent to: {}", toEmail);

        } catch (Exception e) {   // internet issue
            log.error("Email send failed: {}", e.getMessage());
            throw new RuntimeException("Failed to send email!");
        }
    }

    // for rabbit mq..
    @Override
    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            log.info("Email sent to: {}", to);

        } catch (Exception e) {
            log.error("Email send failed: {}", e.getMessage());
            throw new RuntimeException("Failed to send email!");
        }
    }
}