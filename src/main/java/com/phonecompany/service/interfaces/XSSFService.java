package com.phonecompany.service.interfaces;

import java.time.LocalDate;

public interface XSSFService {
    void generateReport(long regionId, LocalDate startDate, LocalDate endDate);
}
