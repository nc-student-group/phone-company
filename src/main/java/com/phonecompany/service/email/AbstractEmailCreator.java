package com.phonecompany.service.email;

import com.phonecompany.model.User;
import com.phonecompany.service.interfaces.MailMessageCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.util.Assert;

/**
 * Base class for all the types providing capabilities
 * of message construction
 *
 * @param <T> message recipient type (e.g. {@link User})
 */
@PropertySource("classpath:mail.properties")
public abstract class AbstractEmailCreator<T extends User>
        implements MailMessageCreator<T> {

    @Value("${mail.address}")
    private String sender;

    /**
     * {@inheritDoc}
     */
    @Override
    public SimpleMailMessage constructMessage(T recipient) {
        this.validateRecipient(recipient);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject(this.getEmailSubject());
        mailMessage.setText(this.getEmailBody(recipient));
        mailMessage.setTo(recipient.getEmail());
        mailMessage.setFrom(this.sender);

        return mailMessage;
    }

    /**
     * Ensures that a {@code SimpleMailMessage} for a recipient
     * object will be properly constructed
     *
     * @param recipient object to be verified
     */
    private void validateRecipient(T recipient) {
        Assert.notNull(recipient.getEmail(), "Email must not be null");
//        Assert.notNull(recipient.getUserName(), "Username must not be null");
    }

    /**
     * Provides text depending on the message type
     *
     * @param  recipient message receiver
     * @return message body
     */
    public abstract String getEmailBody(T recipient);

    /**
     * Provides subject depending on the message type
     *
     * @return message subject
     */
    public abstract String getEmailSubject();
}
