package com.phonecompany.controller;


import com.phonecompany.model.Region;
import com.phonecompany.model.Tariff;
import com.phonecompany.model.TariffRegion;
import com.phonecompany.service.interfaces.RegionService;
import com.phonecompany.service.interfaces.TariffRegionService;
import com.phonecompany.service.interfaces.TariffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private RegionService regionService;

    @Autowired
    private TariffRegionService tariffRegionService;


    @RequestMapping(value = "/api/regions/get", method = RequestMethod.GET)
    public List<Region> getAllRegions() {
        LOGGER.debug("Get all regions.");
        return regionService.getAll();
    }

    @RequestMapping(value = "/api/tariffs/get/by/region/{id}/{page}/{size}", method = RequestMethod.GET)
    public List<TariffRegion> getTariffsByRegionId(@PathVariable("id") Long regionId,
                                                   @PathVariable("page") int page,
                                                   @PathVariable("size") int size) {
        LOGGER.debug("Get all tariffs by region id = " + regionId);
        return tariffRegionService.getAllTariffsByRegionId(regionId, page, size);
    }

    @RequestMapping(value = "/api/tariff/new/get", method = RequestMethod.GET)
    public Tariff getEmptyTariff(){
        return new Tariff();
    }
}
