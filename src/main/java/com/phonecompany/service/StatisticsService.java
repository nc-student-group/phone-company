package com.phonecompany.service;

import com.phonecompany.service.xssfHelper.Statistics;
import com.phonecompany.service.xssfHelper.SheetDataSet;

import java.time.LocalDate;
import java.util.List;

public interface StatisticsService {
    SheetDataSet<LocalDate, Long> prepareStatisticsDataSet(String sheetName,
                                                           List<Statistics> statisticsList,
                                                           LocalDate startDate,
                                                           LocalDate endDate);

}
