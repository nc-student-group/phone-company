package com.phonecompany.service.interfaces;

import org.springframework.mail.SimpleMailMessage;

public interface EmailService {
    /***
     * Sends an email
     */
    void sendMail(SimpleMailMessage message);
}
