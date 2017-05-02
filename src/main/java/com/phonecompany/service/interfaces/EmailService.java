package com.phonecompany.service.interfaces;

import com.phonecompany.model.User;
import org.springframework.mail.SimpleMailMessage;

import java.util.List;

public interface EmailService<T extends User> {
    void sendMail(SimpleMailMessage message, List<T> recipients);

    void sendMail(SimpleMailMessage mailMessage, T recipient);
}
