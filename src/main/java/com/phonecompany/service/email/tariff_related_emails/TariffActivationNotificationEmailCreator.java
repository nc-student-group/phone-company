package com.phonecompany.service.email.tariff_related_emails;

import com.phonecompany.model.Tariff;
import com.phonecompany.service.email.AbstractEmailCreator;
import com.phonecompany.service.interfaces.MailMessageCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class TariffActivationNotificationEmailCreator extends AbstractEmailCreator<Tariff>
        implements MailMessageCreator<Tariff> {

    private TemplateEngine templateEngine;

    @Autowired
    public TariffActivationNotificationEmailCreator(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public String getEmailBody(Tariff tariff) {
        Context context = new Context();
        context.setVariable("tariffName", tariff.getTariffName());
        return this.templateEngine
                .process("tariff-activation-notification", context);
    }

    @Override
    public String getEmailSubject() {
        return "Tariff activation notification";
    }
}
