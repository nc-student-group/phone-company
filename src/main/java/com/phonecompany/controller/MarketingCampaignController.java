package com.phonecompany.controller;

import com.phonecompany.model.MarketingCampaign;
import com.phonecompany.service.interfaces.CustomerService;
import com.phonecompany.service.interfaces.MarketingCampaignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/marketing-campaigns")
public class MarketingCampaignController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MarketingCampaignController.class);

    private MarketingCampaignService marketingCampaignService;
    private CustomerService customerService;

    @Autowired
    public MarketingCampaignController(MarketingCampaignService marketingCampaignService,
                                       CustomerService customerService) {
        this.marketingCampaignService = marketingCampaignService;
        this.customerService = customerService;
    }

    @GetMapping(value = "/available/")
    public List<MarketingCampaign> getMarketingCampaignsAvailableForCustomer() {
        List<MarketingCampaign> marketingCampaigns = marketingCampaignService
                .getMarketingCampaignsAvailableForCustomer(
                        customerService.getCurrentlyLoggedInUser());
        LOGGER.info("Retrieved available marketing campaigns: " + marketingCampaigns);
        return marketingCampaigns;
    }


}
