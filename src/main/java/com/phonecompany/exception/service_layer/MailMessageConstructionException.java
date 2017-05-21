package com.phonecompany.exception.service_layer;

import javax.mail.MessagingException;

/**
 * Thrown if any exceptional situation occurs during an email dispatch
 * (e.g. malformed message body, no sender or receiver specified, etc.).
 */
public class MailMessageConstructionException extends RuntimeException {

    public MailMessageConstructionException(MessagingException e) {
        super("Failed to to send an email", e);
    }
}
