package com.phonecompany.controller;

import com.phonecompany.model.ComplaintStatistics;
import com.phonecompany.model.WeeklyOrderStatistics;
import com.phonecompany.service.interfaces.ComplaintService;
import com.phonecompany.service.interfaces.OrderService;
import com.phonecompany.service.interfaces.TariffService;
import com.phonecompany.service.interfaces.XSSFService;
import com.phonecompany.service.xssfHelper.SheetDataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
    private ComplaintService complaintService;
    private XSSFService<LocalDate, Long> xssfService;

    @Autowired
    public ReportController(TariffService tariffService,
                            OrderService orderService,
                            ComplaintService complaintService,
                            XSSFService<LocalDate, Long> xssfService) {
        this.tariffService = tariffService;
        this.orderService = orderService;
        this.complaintService = complaintService;
        this.xssfService = xssfService;
    }

    @RequestMapping(value = "/{regionId}/{startDate}/{endDate}", method = GET, produces = "application/octet-stream")
    public ResponseEntity<?> getTariffReportByRegionAndTimePeriod(@PathVariable("regionId") Integer regionId,
                                                                  @PathVariable("startDate")
                                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                          LocalDate startDate,
                                                                  @PathVariable("endDate")
                                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                          LocalDate endDate) {

        SheetDataSet<LocalDate, Long> sheetDataSet = this.tariffService
                .prepareStatisticsDataSet(regionId, startDate, endDate);

        LOG.debug("SheetDataSet: {}", sheetDataSet);
        xssfService.generateReport(sheetDataSet);

        InputStream xlsFileInputStream = this.getXlsStreamFromRootDirectory();

        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(xlsFileInputStream));
    }

    @RequestMapping(value = "/complaint/{regionId}/{startDate}/{endDate}", method = GET, produces = "application/vnd.ms-excel")
    public ResponseEntity<?> getComplaintReportByRegionAndTimePeriod(@PathVariable("regionId") Integer regionId,
                                                                     @PathVariable("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                     @PathVariable("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        LOG.debug("Generation complaint report");
        SheetDataSet sheetDataSet = this.complaintService
                .prepareComplaintReportDataSet(regionId, startDate, endDate);

        LOG.debug("SheetDataSet: {}", sheetDataSet);
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
            return new FileInputStream(files[files.length - 1]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/order-statistics")
    public ResponseEntity<?> getOrderStatisticsForTheLastMonthByWeeks() {

        WeeklyOrderStatistics orderStatistics = this.orderService.getOrderStatistics();

        return new ResponseEntity<>(orderStatistics, HttpStatus.OK);
    }

    @GetMapping("/complaints-statistics")
    public ResponseEntity<?> getComplaintStatisticsForTheLastMonthByWeeks() {
        LOG.debug("Get complaints statistic");
        ComplaintStatistics complaintStatistics = this.complaintService.getComplaintStatistics();

        return new ResponseEntity<>(complaintStatistics, HttpStatus.OK);
    }
}
