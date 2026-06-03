package com.fundoonotes.fundoo_notes.service.implementation;

import com.fundoonotes.fundoo_notes.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Value("${BREVO_API_KEY}")
    private String apiKey;

    @Value("${MAIL_FROM}")
    private String fromEmail;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void sendOtpEmail(String toEmail, String otp) {
        String body = "Hello!\n\nYour OTP for password reset is: "
                + otp
                + "\n\nThis OTP is valid for 10 minutes only."
                + "\n\nIf you did not request this, please ignore this email."
                + "\n\nRegards,\nFundoo Notes Team";
        sendEmail(toEmail, "Fundoo Notes - Password Reset OTP", body);
    }

    @Override
    public void sendEmail(String to, String subject, String body) {
        try {
            log.info("Sending email via Brevo API FROM: {} TO: {}", fromEmail, to);

            HttpHeaders headers = new HttpHeaders();
            headers.set("api-key", apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            // escape newlines for JSON
            String safeBody = body.replace("\n", "\\n");

            String payload = "{"
                    + "\"sender\":{\"email\":\"" + fromEmail + "\"},"
                    + "\"to\":[{\"email\":\"" + to + "\"}],"
                    + "\"subject\":\"" + subject + "\","
                    + "\"textContent\":\"" + safeBody + "\""
                    + "}";

            HttpEntity<String> request = new HttpEntity<>(payload, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://api.brevo.com/v3/smtp/email",
                    request,
                    String.class
            );

            log.info("Email sent successfully! Status: {}", response.getStatusCode());

        } catch (Exception e) {
            log.error("Email send failed: {}", e.getMessage());
            throw new RuntimeException("Failed to send email!");
        }
    }
}
