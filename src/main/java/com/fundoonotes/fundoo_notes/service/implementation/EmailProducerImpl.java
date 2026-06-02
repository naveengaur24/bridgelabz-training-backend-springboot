package com.fundoonotes.fundoo_notes.service.implementation;
import com.fundoonotes.fundoo_notes.config.RabbitMQConfig;
import com.fundoonotes.fundoo_notes.dto.EmailMessageDTO;
import com.fundoonotes.fundoo_notes.service.EmailProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailProducerImpl implements EmailProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Override
    public void sendEmailMessage(String to, String subject, String body) {
        // EmailMessageDTO create kia..
        EmailMessageDTO message = new EmailMessageDTO(to, subject, body);

        // RabbitMQ Queue mein bhejega..
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EMAIL_EXCHANGE,    // it decide message kis queuq m jayega
                RabbitMQConfig.EMAIL_ROUTING_KEY,     // is routing wali queue m bjenege
                message  // actual data..
        );
        log.info("Email message sent to queue for: {}", to);
    }
}