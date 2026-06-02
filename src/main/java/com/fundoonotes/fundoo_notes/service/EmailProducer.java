package com.fundoonotes.fundoo_notes.service;

public interface EmailProducer {
    void sendEmailMessage(String to, String subject, String body);
}