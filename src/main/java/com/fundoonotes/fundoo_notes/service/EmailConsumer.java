package com.fundoonotes.fundoo_notes.service;

import com.fundoonotes.fundoo_notes.dto.EmailMessageDTO;

public interface EmailConsumer {
    void consumeEmailMessage(EmailMessageDTO message);
}