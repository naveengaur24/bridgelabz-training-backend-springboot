package com.fundoonotes.fundoo_notes.service;

public interface EmailService {
    void sendOtpEmail(String toEmail, String otp);
    void sendEmail(String to, String subject, String body);
}