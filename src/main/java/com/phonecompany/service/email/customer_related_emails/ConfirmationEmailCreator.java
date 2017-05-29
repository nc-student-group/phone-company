package com.phonecompany.service.email.customer_related_emails;

import com.phonecompany.model.VerificationToken;
import com.phonecompany.service.email.AbstractEmailCreator;
import com.phonecompany.service.interfaces.MailMessageCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class ConfirmationEmailCreator extends AbstractEmailCreator<VerificationToken>
        implements MailMessageCreator<VerificationToken> {

    private static final Logger LOG = LoggerFactory.getLogger(ConfirmationEmailCreator.class);

    @Value("${application-url}")
    private String applicationUrl;
    private TemplateEngine templateEngine;

    @Autowired
    public ConfirmationEmailCreator(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEmailBody(VerificationToken entity) {
        Context context = new Context();
        String verificationUrl = applicationUrl + "/verifyRegistration?token="
                + entity.getToken();

        LOG.debug("Verification url: {}", verificationUrl);
        context.setVariable("verificationUrl", verificationUrl);

        return this.templateEngine
                .process("email-template", context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEmailSubject() {
        return "Registration confirmation";
    }
}
