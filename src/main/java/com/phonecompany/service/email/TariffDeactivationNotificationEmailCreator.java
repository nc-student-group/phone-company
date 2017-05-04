package com.phonecompany.service.email;

import com.phonecompany.model.Tariff;
import com.phonecompany.service.interfaces.MailMessageCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component("tariffDeactivationEmailCreator")
public class TariffDeactivationNotificationEmailCreator extends AbstractEmailCreator<Tariff>
        implements MailMessageCreator<Tariff> {

    private static final Logger LOG = LoggerFactory.getLogger(TariffDeactivationNotificationEmailCreator.class);

    private TemplateEngine templateEngine;

    @Autowired
    public TariffDeactivationNotificationEmailCreator(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public String getEmailBody(Tariff tariff) {
        Context context = new Context();
        context.setVariable("tariffName", tariff.getTariffName());
        return this.templateEngine
                .process("tariff-deactivation-notification", context);
    }

    @Override
    public String getEmailSubject() {
        return "Tariff deactivation";
    }
}
