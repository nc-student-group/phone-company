package com.phonecompany.service.email.csr_related_emails;

import com.phonecompany.model.MarketingCampaign;
import com.phonecompany.service.email.AbstractEmailCreator;
import com.phonecompany.service.interfaces.MailMessageCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class MarketingCampaignActivationNotificationEmailCreator extends AbstractEmailCreator<MarketingCampaign>
        implements MailMessageCreator<MarketingCampaign> {

    private TemplateEngine templateEngine;

    @Autowired
    public MarketingCampaignActivationNotificationEmailCreator(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public String getEmailBody(MarketingCampaign marketingCampaign) {
        Context context = new Context();
        context.setVariable("campaignName", marketingCampaign.getName());
        return this.templateEngine
                .process("marketing-campaign-activation-notification", context);
    }

    @Override
    public String getEmailSubject() {
        return "Marketing campaign activation notification";
    }
}
