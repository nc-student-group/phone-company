package com.phonecompany.service.email;

import com.phonecompany.model.User;
import com.phonecompany.service.interfaces.MailMessageCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.util.Assert;

@PropertySource("classpath:mail.properties")
public abstract class AbstractEmailCreator implements MailMessageCreator {

    @Value("${mail.address}")
    private String sender;

    @Override
    public SimpleMailMessage constructMessage(User recipient) {
        this.validateUser(recipient);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject(this.getEmailSubject());
        mailMessage.setText(this.getEmailBody(recipient));
        mailMessage.setTo(recipient.getEmail());
        mailMessage.setFrom(this.sender);

        return mailMessage;
    }

    private void validateUser(User user) {
        Assert.notNull(user.getEmail(), "Email must not be null");
        Assert.notNull(user.getUserName(), "Username must not be null");
    }

    public abstract String getEmailBody(User recipient);
    public abstract String getEmailSubject();
}
