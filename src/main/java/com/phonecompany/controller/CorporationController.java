package com.phonecompany.controller;

import com.phonecompany.model.Corporate;
import com.phonecompany.service.interfaces.CorporateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
public class CorporationController {
    
    private static final Logger LOG = LoggerFactory.getLogger(CorporationController.class);
    private CorporateService corporateService;

    @Autowired
    public CorporationController(CorporateService corporateService) {
        this.corporateService = corporateService;
    }

    @RequestMapping(method = GET, value = "/api/corporations/{page}/{size}")
    public Map<String, Object> getAllCorporations(@PathVariable("page") int page,
                                                    @PathVariable("size") int size,
                                                    @RequestParam("s") String partOfName) {
        LOG.info("Retrieving all the corporations contained in the database");

        List<Corporate> corporates = this.corporateService.getAllCorporatePaging(page,size,partOfName);

        LOG.info("Corporates fetched from the database: " + corporates);
        Map<String, Object> response = new HashMap<>();
        response.put("corporates", corporates);
        response.put("corporatesSelected", corporateService.getCountCorporates(partOfName));
        return response;
    }

    @RequestMapping(method = PUT, value = "/api/corporations")
    public ResponseEntity<?> saveEditedCorporationByAdmin(@RequestBody Corporate corporate) {
        LOG.debug("Corporate retrieved from the http request: " + corporate);
        Corporate persistedCorporate = this.corporateService.update(corporate);
        return new ResponseEntity<>(persistedCorporate, HttpStatus.CREATED);
    }

    @RequestMapping(method = POST, value = "/api/corporation/save")
    public ResponseEntity<?> saveCorporationByAdmin(@RequestBody Corporate corporate) {
        LOG.debug("Corporate retrieved from the http request: " + corporate);
        Corporate persistedCorporate = this.corporateService.save(corporate);
        return new ResponseEntity<>(persistedCorporate, HttpStatus.CREATED);
    }
}
