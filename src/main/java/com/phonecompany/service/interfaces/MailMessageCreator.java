package com.phonecompany.service.interfaces;

import org.springframework.mail.SimpleMailMessage;

/**
 * Defines a common behaviour for all the classes
 * capable of constructing messages for specific
 * entities of type <T>
 */
public interface MailMessageCreator<T> {
    /**
     * Constructs message for the specific recipient
     *
     * @return fully constructed {@code SimpleMailMessage}
     *         object
     */
    SimpleMailMessage constructMessage(T entity);
}
