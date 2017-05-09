package com.phonecompany.controller;

import com.phonecompany.service.interfaces.XSSFService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.time.LocalDate;
import java.time.Month;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class ReportController {

    private static final Logger LOG = LoggerFactory.getLogger(ReportController.class);

    private XSSFService xssfService;

    @Autowired
    public ReportController(XSSFService xssfService) {
        this.xssfService = xssfService;
    }

    @RequestMapping(value = "/api/reports/{regionId}", method = GET, produces = "application/vnd.ms-excel")
    public ResponseEntity<?> getReportByRegionAndTimePeriod(@PathVariable("regionId") Integer regionId) {
        LOG.debug("Generating xls report");
        LOG.debug("Input regionId parameter: {}", regionId);
        LocalDate startDate = LocalDate.of(2017, Month.MAY, 1);
        LocalDate topDate = LocalDate.of(2017, Month.MAY, 5);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        this.xssfService.generateReport(1, startDate, topDate);
        InputStream xlsFileInputStream = this.getXlsFileInputStream();

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(xlsFileInputStream));
    }

    private InputStream getXlsFileInputStream() {
        try {
            File rootDir = new File("./");
            File[] files = rootDir.listFiles((dir, filename) -> filename.endsWith(".xlsx"));
            File file = files[0];
            LOG.debug("Report file: {}", file);
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
