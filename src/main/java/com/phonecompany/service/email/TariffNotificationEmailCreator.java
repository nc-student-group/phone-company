package com.phonecompany.service.email;

import com.phonecompany.model.Tariff;
import com.phonecompany.service.interfaces.MailMessageCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component("tariffNotificationEmailCreator")
public class TariffNotificationEmailCreator extends AbstractEmailCreator<Tariff>
        implements MailMessageCreator<Tariff> {

    private static final Logger LOG = LoggerFactory.getLogger(TariffNotificationEmailCreator.class);

    private TemplateEngine templateEngine;

    @Autowired
    public TariffNotificationEmailCreator(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public String getEmailBody(Tariff tariff) {
        Context context = new Context();
        context.setVariable("tariffName", tariff.getTariffName());
        context.setVariable("tariffName", tariff.getPrice());
        return this.templateEngine
                .process("service-activation-notification", context);
    }

    @Override
    public String getEmailSubject() {
        return "New tariff notification";
    }
}
