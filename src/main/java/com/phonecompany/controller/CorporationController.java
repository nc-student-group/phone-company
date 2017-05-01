package com.phonecompany.controller;

import com.phonecompany.model.Corporate;
import com.phonecompany.service.interfaces.CorporateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class CorporationController {
    
    private static final Logger LOG = LoggerFactory.getLogger(CorporationController.class);
    private CorporateService corporateService;

    @Autowired
    public CorporationController(CorporateService corporateService) {
        this.corporateService = corporateService;
    }

    @RequestMapping(method = GET, value = "/api/corporations")
    public Collection<Corporate> getAllCorporations() {
        LOG.info("Retrieving all the corporations contained in the database");

        List<Corporate> corporates = this.corporateService.getAll();

        LOG.info("Corporates fetched from the database: " + corporates);

        return Collections.unmodifiableCollection(corporates);
    }

    @RequestMapping(method = POST, value = "/api/corporation/save")
    public ResponseEntity<?> saveCustomerByAdmin(@RequestBody Corporate corporate) {
        LOG.debug("Corporate retrieved from the http request: " + corporate);
        Corporate persistedCorporate = this.corporateService.save(corporate);
        return new ResponseEntity<>(persistedCorporate, HttpStatus.CREATED);
    }
}
