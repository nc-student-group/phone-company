package com.phonecompany.controller;

import com.phonecompany.model.Tariff;
import com.phonecompany.model.TariffRegion;
import com.phonecompany.service.interfaces.TariffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/tariff-region")
public class TariffRegionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TariffRegionController.class);

    private TariffService tariffService;

    @Autowired
    public TariffRegionController(TariffService tariffService){
        this.tariffService = tariffService;
    }

    @PostMapping
    public ResponseEntity<?> saveTariffRegion(@RequestBody List<TariffRegion> tariffRegions) {
        Tariff savedTariff = tariffService.addNewTariff(tariffRegions);
        return new ResponseEntity<Object>(savedTariff, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateTariff(@RequestBody List<TariffRegion> tariffRegions) {
        Tariff updatedTariff = tariffService.updateTariff(tariffRegions);
        return new ResponseEntity<Object>(updatedTariff, HttpStatus.OK);
    }
}
