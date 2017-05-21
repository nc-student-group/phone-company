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
@RequestMapping(value = "/api/corporations")
public class CorporationController {

    private static final Logger LOG = LoggerFactory.getLogger(CorporationController.class);
    private CorporateService corporateService;

    @Autowired
    public CorporationController(CorporateService corporateService) {
        this.corporateService = corporateService;
    }

    @RequestMapping(method = GET, value = "/{page}/{size}")
    public Map<String, Object> getAllCorporationsPaging(@PathVariable("page") int page,
                                                        @PathVariable("size") int size,
                                                        @RequestParam("s") String partOfName) {
        return this.corporateService.getAllCorporatePaging(page, size, partOfName);
    }

    @RequestMapping(method = GET)
    public List<Corporate> getAllCorporations() {
        LOG.info("Retrieving all the corporations contained in the database");

        List<Corporate> corporates = this.corporateService.getAll();

        LOG.info("Corporates fetched from the database: " + corporates);
        return corporates;
    }

    @RequestMapping(method = GET,value = "/{id}")
    public Corporate getCorporationById(@PathVariable("id") long id) {
        LOG.info("Retrieving corporation by id");

        Corporate corporate = this.corporateService.getById(id);

        LOG.info("Corporates fetched from the database: " + corporate);
        return corporate;
    }

    @RequestMapping(method = PUT)
    public ResponseEntity<?> saveEditedCorporationByAdmin(@RequestBody Corporate corporate) {
        LOG.debug("Corporate retrieved from the http request: " + corporate);
        Corporate persistedCorporate = this.corporateService.update(corporate);
        return new ResponseEntity<>(persistedCorporate, HttpStatus.CREATED);
    }

    @RequestMapping(method = POST)
    public ResponseEntity<?> saveCorporationByAdmin(@RequestBody Corporate corporate) {
        LOG.debug("Corporate retrieved from the http request: " + corporate);
        Corporate persistedCorporate = this.corporateService.save(corporate);
        return new ResponseEntity<>(persistedCorporate, HttpStatus.CREATED);
    }
}
