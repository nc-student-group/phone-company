package com.phonecompany.controller;


import com.phonecompany.model.Region;
import com.phonecompany.model.Tariff;
import com.phonecompany.model.TariffRegion;
import com.phonecompany.model.enums.ProductStatus;
import com.phonecompany.service.interfaces.RegionService;
import com.phonecompany.service.interfaces.TariffRegionService;
import com.phonecompany.service.interfaces.TariffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private RegionService regionService;

    @Autowired
    private TariffRegionService tariffRegionService;

    @Autowired
    private TariffService tariffService;


    @RequestMapping(value = "/api/regions/get", method = RequestMethod.GET)
    public List<Region> getAllRegions() {
        LOGGER.debug("Get all regions.");
        return regionService.getAll();
    }

    @RequestMapping(value = "/api/tariffs/get/by/region/{id}/{page}/{size}", method = RequestMethod.GET)
    public Map<String, Object> getTariffsByRegionId(@PathVariable("id") Long regionId,
                                                    @PathVariable("page") int page,
                                                    @PathVariable("size") int size) {
        LOGGER.debug("Get all tariffs by region id = " + regionId);
        Map<String, Object> response = new HashMap<>();
        response.put("tariffs", tariffRegionService.getAllTariffsByRegionId(regionId, page, size));
        response.put("tariffsSelected", tariffRegionService.getCountTariffsByRegionId(regionId));
        return response;
    }

    @RequestMapping(value = "/api/tariff/new/get", method = RequestMethod.GET)
    public Tariff getEmptyTariff() {
        return new Tariff();
    }

    @RequestMapping(value = "/api/tariff/add", method = RequestMethod.POST)
    public ResponseEntity<Void> saveTariff(@RequestBody List<TariffRegion> tariffRegions) {

        if (tariffRegions.size() > 0) {
            Tariff tariff = tariffRegions.get(0).getTariff();
            tariff.setProductStatus(ProductStatus.ACTIVATED);
            Tariff savedTariff = tariffService.save(tariff);
            LOGGER.debug("Tariff added {}", tariff);
            tariffRegions.forEach((TariffRegion tariffRegion) -> {
                if(tariffRegion.getPrice() > 0 && tariffRegion.getRegion() != null) {
                    tariffRegion.setTariff(savedTariff);
                    tariffRegionService.save(tariffRegion);
                    LOGGER.debug("Tariff-region added {}", tariffRegion);
                }
            });
        }
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
