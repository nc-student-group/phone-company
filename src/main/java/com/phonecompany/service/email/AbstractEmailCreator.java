package com.phonecompany.service.email;

import com.phonecompany.model.User;
import com.phonecompany.service.interfaces.MailMessageCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;

import static com.phonecompany.service.email.EmailDispatchTask.PHONE_COMPANY_URL;

/**
 * Base class for all the types providing capabilities
 * of message construction
 *
 * @param <T> message recipient type (e.g. {@link User})
 */
@PropertySource("classpath:mail.properties")
public abstract class AbstractEmailCreator<T>
        implements MailMessageCreator<T> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractEmailCreator.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public SimpleMailMessage constructMessage(T entity) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject(this.getEmailSubject());
        mailMessage.setText(this.getEmailBody(entity));
        mailMessage.setFrom(PHONE_COMPANY_URL);
        return mailMessage;
    }

    /**
     * Provides text depending on the message type
     *
     * @return message body
     */
    public abstract String getEmailBody(T entity);

    /**
     * Provides subject depending on the message type
     *
     * @return message subject
     */
    public abstract String getEmailSubject();
}
