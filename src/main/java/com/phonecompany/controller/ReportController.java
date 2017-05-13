package com.phonecompany.controller;

import com.phonecompany.model.OrderStatistics;
import com.phonecompany.service.interfaces.OrderService;
import com.phonecompany.service.interfaces.TariffService;
import com.phonecompany.service.interfaces.XSSFService;
import com.phonecompany.service.xssfHelper.SheetDataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.time.LocalDate;

import static com.phonecompany.util.FileUtil.getFilesWithExtensionFromPath;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value = "api/reports")
public class ReportController {

    private static final Logger LOG = LoggerFactory.getLogger(ReportController.class);
    private static final String EXCEL_EXTENSION = "xlsx";
    private static final String CURRENT_DIRECTORY = "./";

    private TariffService tariffService;
    private OrderService orderService;
    private XSSFService xssfService;

    @Autowired
    public ReportController(TariffService tariffService,
                            OrderService orderService,
                            XSSFService xssfService) {
        this.tariffService = tariffService;
        this.orderService = orderService;
        this.xssfService = xssfService;
    }

    @RequestMapping(value = "/{regionId}/{startDate}/{endDate}", method = GET, produces = "application/vnd.ms-excel")
    public ResponseEntity<?> getReportByRegionAndTimePeriod(@PathVariable("regionId") Integer regionId,
            @PathVariable("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @PathVariable("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        LOG.debug("Generating report");
        SheetDataSet sheetDataSet = this.tariffService
                .prepareTariffReportDataSet(regionId, startDate, endDate);

        xssfService.generateReport(sheetDataSet);

        InputStream xlsFileInputStream = this.getXlsStreamFromRootDirectory();

        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(xlsFileInputStream));
    }

    private InputStream getXlsStreamFromRootDirectory() {
        try {
            File[] files = getFilesWithExtensionFromPath(EXCEL_EXTENSION, CURRENT_DIRECTORY);
            return new FileInputStream(files[0]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/order-statistics")
    public ResponseEntity<?> getOrderStatisticsForTheLastMonthByWeeks() {

        OrderStatistics orderStatistics = this.orderService.getOrderStatistics();

        return new ResponseEntity<>(orderStatistics, HttpStatus.OK);
    }
}
