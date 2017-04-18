package com.phonecompany.service.email;

import com.phonecompany.model.User;
import com.phonecompany.service.interfaces.MailMessageCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.UUID;

@Component("confirmationEmailCreator")
public class ConfirmationEmailCreator extends AbstractEmailCreator
        implements MailMessageCreator {

    private static final Logger LOG = LoggerFactory.getLogger(ConfirmationEmailCreator.class);

    @Value("${application-url}")
    private String applicationUrl;
    private TemplateEngine templateEngine;

    @Autowired
    public ConfirmationEmailCreator(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public String getEmailBody(User recipient) {
        String randomID = UUID.randomUUID().toString();
        String confirmationUrl = applicationUrl + "/confirmRegistration?token=" + randomID;
        LOG.info("Confirmation url: {}", confirmationUrl);

//        Context context = new Context();
//        context.setVariable("name", recipient.getUserName());
//        context.setVariable("body", confirmationUrl);
//
//        return this.templateEngine
//                .process("email-template", context);
        return confirmationUrl;
    }

    @Override
    public String getEmailSubject() {
        return "Registration confirmation";
    }
}
