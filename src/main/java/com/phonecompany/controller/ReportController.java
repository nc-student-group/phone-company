package com.phonecompany.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping(value = "/api")
public class ReportController {

    private static final Logger LOG = LoggerFactory.getLogger(ReportController.class);

    @GetMapping("/{regionId}/{beginDate}/{endDate}")
    public ResponseEntity<?> getReportByRegionAndTimePeriod(int regionId,
                                                            @PathVariable LocalDate beginDate,
                                                            @PathVariable LocalDate endDate) {
        LOG.debug("Generating xls report");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
