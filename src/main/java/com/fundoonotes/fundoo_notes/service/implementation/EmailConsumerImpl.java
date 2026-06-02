package com.fundoonotes.fundoo_notes.service.implementation;

import com.fundoonotes.fundoo_notes.config.RabbitMQConfig;
import com.fundoonotes.fundoo_notes.dto.EmailMessageDTO;
import com.fundoonotes.fundoo_notes.service.EmailConsumer;
import com.fundoonotes.fundoo_notes.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailConsumerImpl implements EmailConsumer {

    @Autowired
    private EmailService emailService;

    @Override
    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)     // queue m jo bhi message aaye automatic method run krega..
    public void consumeEmailMessage(EmailMessageDTO message) {
        log.info("Email message received from queue for: {}", message.getTo());

        // Actual email bhejhega..  SMTP Gmail call
        emailService.sendEmail(
                message.getTo(),
                message.getSubject(),
                message.getBody()
        );

        log.info("Email sent successfully to: {}", message.getTo());
    }
}