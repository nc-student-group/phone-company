package com.phonecompany.controller;

import com.phonecompany.model.WeeklyComplaintStatistics;
import com.phonecompany.model.WeeklyOrderStatistics;
import com.phonecompany.service.interfaces.*;
import com.phonecompany.service.xssfHelper.BookDataSet;
import com.phonecompany.service.xssfHelper.SheetDataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.time.LocalDate;

import static com.phonecompany.service.xssfHelper.SheetDataSet.combine;

@RestController
@RequestMapping(value = "api/reports")
public class ReportController {

    private static final Logger LOG = LoggerFactory.getLogger(ReportController.class);

    private TariffService tariffService;
    private ServiceService serviceService;
    private OrderService orderService;
    private ComplaintService complaintService;
    private XSSFService<LocalDate, Long> xssfService;

    @Autowired
    public ReportController(TariffService tariffService,
                            ServiceService serviceService,
                            OrderService orderService,
                            ComplaintService complaintService,
                            XSSFService<LocalDate, Long> xssfService) {
        this.tariffService = tariffService;
        this.serviceService = serviceService;
        this.orderService = orderService;
        this.complaintService = complaintService;
        this.xssfService = xssfService;
    }

    @GetMapping(value = "orders/{regionId}/{startDate}/{endDate}", produces = "application/octet-stream")
    public ResponseEntity<?> getOrderReportByRegionAndTimePeriod(@PathVariable("regionId") Integer regionId,
                                                                 @PathVariable("startDate") LocalDate startDate,
                                                                 @PathVariable("endDate") LocalDate endDate) {
        SheetDataSet<LocalDate, Long> tariffStatisticsDataSet = this.tariffService
                .getTariffStatisticsDataSet(regionId, startDate, endDate);

        SheetDataSet<LocalDate, Long> servicesStatisticsDataSet = this.serviceService
                .getServiceStatisticsDataSet(startDate, endDate);

        BookDataSet<LocalDate, Long> bookDataSet = combine(tariffStatisticsDataSet,
                servicesStatisticsDataSet);

        InputStream reportInputStream = xssfService.generateReport(bookDataSet);

        return this.getOctetStreamResponseEntity(reportInputStream);

    }

    @GetMapping(value = "/complaints/{regionId}/{startDate}/{endDate}", produces = "application/octet-stream")
    public ResponseEntity<?> getComplaintReportByRegionAndTimePeriod(@PathVariable("regionId") Integer regionId,
                                                                     @PathVariable("startDate") LocalDate startDate,
                                                                     @PathVariable("endDate") LocalDate endDate) {
        SheetDataSet<LocalDate, Long> complaintStatisticsDataSet = this.complaintService
                .getComplaintStatisticsDataSet(regionId, startDate, endDate);

        BookDataSet<LocalDate, Long> bookDataSet = new BookDataSet<>();

        InputStream reportInputStream = xssfService
                .generateReport(bookDataSet.addSheet(complaintStatisticsDataSet));

        return this.getOctetStreamResponseEntity(reportInputStream);
    }

    @GetMapping("/order-statistics")
    public ResponseEntity<?> getOrderStatisticsForTheLastMonthByWeeks() {

        WeeklyOrderStatistics orderStatistics = this.orderService.getOrderStatistics();

        return new ResponseEntity<>(orderStatistics, HttpStatus.OK);
    }

    @GetMapping("/complaints-statistics")
    public ResponseEntity<?> getComplaintStatisticsForTheLastMonthByWeeks() {
        LOG.debug("Get complaints statistic");
        WeeklyComplaintStatistics complaintStatistics = this.complaintService.getComplaintStatistics();

        return new ResponseEntity<>(complaintStatistics, HttpStatus.OK);
    }

    private ResponseEntity<?> getOctetStreamResponseEntity(InputStream inputStream) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(inputStream));
    }
}
