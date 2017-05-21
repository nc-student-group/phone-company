package com.phonecompany.controller;

import com.phonecompany.model.Customer;
import com.phonecompany.model.MarketingCampaign;
import com.phonecompany.service.interfaces.CustomerService;
import com.phonecompany.service.interfaces.EmailService;
import com.phonecompany.service.interfaces.MailMessageCreator;
import com.phonecompany.service.interfaces.MarketingCampaignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/marketing-campaigns")
public class MarketingCampaignController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MarketingCampaignController.class);

    private MarketingCampaignService marketingCampaignService;
    private CustomerService customerService;
    private EmailService<Customer> emailService;
    private MailMessageCreator<MarketingCampaign> marketingCampaignMailMessageCreator;

    @Autowired
    public MarketingCampaignController(MarketingCampaignService marketingCampaignService,
                                       CustomerService customerService,
                                       EmailService<Customer> emailService,
                                       @Qualifier("marketingCampaignActivationNotificationEmailCreator")
                                                   MailMessageCreator<MarketingCampaign>
                                                   marketingCampaignMailMessageCreator) {
        this.marketingCampaignService = marketingCampaignService;
        this.customerService = customerService;
        this.emailService = emailService;
        this.marketingCampaignMailMessageCreator = marketingCampaignMailMessageCreator;
    }

    @GetMapping(value = "/available/")
    public List<MarketingCampaign> getMarketingCampaignsAvailableForCustomer() {
        List<MarketingCampaign> marketingCampaigns = marketingCampaignService
                .getMarketingCampaignsAvailableForCustomer(
                        customerService.getCurrentlyLoggedInUser());
        LOGGER.info("Retrieved available marketing campaigns: " + marketingCampaigns);
        return marketingCampaigns;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getMarketingCampaignById(
            @PathVariable("id") Long marketingCampaignId) {
        MarketingCampaign marketingCampaign = marketingCampaignService
                .getById(marketingCampaignId);
        LOGGER.info("Retrieved marketing campaign: " + marketingCampaign);
        return new ResponseEntity<>(marketingCampaign, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/activate")
    public ResponseEntity<?> activateMarketingCampaign(
            @PathVariable("id") Long marketingCampaignId) {
        Customer currentlyLoggedInUser = customerService.getCurrentlyLoggedInUser();
        MarketingCampaign marketingCampaign = marketingCampaignService
                .getById(marketingCampaignId);
        marketingCampaignService.activateMarketingCampaign(marketingCampaign,
                currentlyLoggedInUser);
        SimpleMailMessage notificationMessage = this.marketingCampaignMailMessageCreator
                .constructMessage(marketingCampaign);
        this.emailService.sendMail(notificationMessage, currentlyLoggedInUser);
        LOGGER.info("Retrieved marketing campaign: " + marketingCampaign);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
