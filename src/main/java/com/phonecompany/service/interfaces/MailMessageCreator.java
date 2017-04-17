package com.phonecompany.service.interfaces;

import com.phonecompany.model.User;
import org.springframework.mail.SimpleMailMessage;

public interface MailMessageCreator {
    SimpleMailMessage constructMessage(User recipient);
}
