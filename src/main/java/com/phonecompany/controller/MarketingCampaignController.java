package com.phonecompany.controller;

import com.phonecompany.annotations.Cacheable;
import com.phonecompany.model.Customer;
import com.phonecompany.model.MarketingCampaign;
import com.phonecompany.model.MarketingCampaignServices;
import com.phonecompany.model.Service;
import com.phonecompany.model.enums.ProductStatus;
import com.phonecompany.service.email.csr_related_emails.MarketingCampaignActivationNotificationEmailCreator;
import com.phonecompany.service.interfaces.CustomerService;
import com.phonecompany.service.interfaces.EmailService;
import com.phonecompany.service.interfaces.MarketingCampaignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/marketing-campaigns")
public class MarketingCampaignController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MarketingCampaignController.class);

    private MarketingCampaignService marketingCampaignService;
    private CustomerService customerService;
    private EmailService<Customer> emailService;
    private MarketingCampaignActivationNotificationEmailCreator marketingCampaignMailMessageCreator;

    @Autowired
    public MarketingCampaignController(MarketingCampaignService marketingCampaignService,
                                       CustomerService customerService,
                                       EmailService<Customer> emailService,
                                       MarketingCampaignActivationNotificationEmailCreator marketingCampaignMailMessageCreator) {
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

    @GetMapping(value = "/available/{regionId}")
    public List<MarketingCampaign> getAvailableMarketingCampaignsByRegionId(
            @PathVariable("regionId") long regionId) {
        List<MarketingCampaign> marketingCampaigns = marketingCampaignService
                .getAvailableMarketingCampaignsByRegionId(regionId);
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

    @GetMapping(value = "/{page}/{size}")
    public Map<String, Object> getTariffs(@PathVariable("page") int page,
                                          @PathVariable("size") int size,
                                          @RequestParam("n") String name,
                                          @RequestParam("s") int status) {
        LOGGER.info("Trying to retrieve marketing campaigns...");
        return marketingCampaignService.getMarketingCampaignsTable(page, size, name, status);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> updateTariffStatus(@PathVariable("id") long marketingCampaignId,
                                                @RequestBody String productStatus) {
        this.marketingCampaignService.updateMarketingCampaignStatus(
                marketingCampaignId, ProductStatus.valueOf(productStatus));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/empty")
    public MarketingCampaign getEmptyMarketingCampaign() {
        return new MarketingCampaign();
    }

    @PostMapping
    public ResponseEntity<?> addMarketingCampaign(@RequestBody MarketingCampaign campaign) {
        campaign.setMarketingCampaignStatus(ProductStatus.ACTIVATED);
        MarketingCampaign persistedCampaign = this.marketingCampaignService.save(campaign);
        return new ResponseEntity<>(persistedCampaign, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateMarketingCampaign(@RequestBody MarketingCampaign campaign) {
        MarketingCampaign persistedCampaign = this.marketingCampaignService.update(campaign);
        return new ResponseEntity<>(persistedCampaign, HttpStatus.OK);
    }
}
