package com.phonecompany.controller;

import com.phonecompany.model.Region;
import com.phonecompany.service.interfaces.RegionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/regions")
public class RegionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegionController.class);
    private RegionService regionService;

    @Autowired
    public RegionController(RegionService regionService){
        this.regionService = regionService;
    }

    @GetMapping
    public List<Region> getAllRegions() {
        LOGGER.debug("Get all regions.");
        return regionService.getAll();
    }
}
