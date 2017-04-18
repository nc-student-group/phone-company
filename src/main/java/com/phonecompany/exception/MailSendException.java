package com.phonecompany.exception;

import javax.mail.MessagingException;

public class MailSendException extends RuntimeException {

    public MailSendException(MessagingException e) {
        super("Failed to to send email.", e);
    }

}
