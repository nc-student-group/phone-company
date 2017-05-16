package com.phonecompany.controller;


import com.phonecompany.model.MarketingCampaignServices;
import com.phonecompany.model.MarketingCampaignTariff;
import com.phonecompany.service.interfaces.CustomerService;
import com.phonecompany.service.interfaces.MarketingCampaignServicesService;
import com.phonecompany.service.interfaces.MarketingCampaignTariffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/marketing-campaigns")
public class MarketingCampaignController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MarketingCampaignController.class);

    private MarketingCampaignTariffService marketingCampaignTariffService;
    private MarketingCampaignServicesService marketingCampaignServicesService;
    private CustomerService customerService;

    @Autowired
    public MarketingCampaignController(MarketingCampaignTariffService marketingCampaignTariffService,
                                       MarketingCampaignServicesService marketingCampaignServicesService,
                                       CustomerService customerService) {
        this.marketingCampaignTariffService = marketingCampaignTariffService;
        this.marketingCampaignServicesService = marketingCampaignServicesService;
        this.customerService = customerService;
    }

    @GetMapping(value = "/available/")
    public ResponseEntity<?> getMarketingCampaignsAvailableForCustomer() {
        List<MarketingCampaignTariff> marketingCampaignTariffs = marketingCampaignTariffService
                .getMarketingCampaignTariffsAvailableForCustomer(
                        customerService.getCurrentlyLoggedInUser());
        LOGGER.info("Retrieved available marketing campaign tariffs: " + marketingCampaignTariffs);

        List<MarketingCampaignServices> marketingCampaignServices = new ArrayList<>();
        marketingCampaignTariffs.forEach(
                mct -> marketingCampaignServices.addAll(marketingCampaignServicesService
                .getMarketingCampaignServicesByMarketingCampaign(mct.getMarketingCampaign())));
        LOGGER.info("Retrieved available marketing campaign services: "
                + marketingCampaignServices);

        Map<String, Object> response = new HashMap<>();
        response.put("marketingCampaignTariffs", marketingCampaignTariffs);
        response.put("marketingCampaignServices", marketingCampaignServices);

        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }


}
