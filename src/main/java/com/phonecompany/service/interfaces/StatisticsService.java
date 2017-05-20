package com.phonecompany.service.interfaces;

import com.phonecompany.service.xssfHelper.Statistics;
import com.phonecompany.service.xssfHelper.SheetDataSet;

import java.time.LocalDate;
import java.util.List;

public interface StatisticsService<K, V> {
    SheetDataSet<K, V> prepareStatisticsDataSet(String sheetName,
                                                List<Statistics> statisticsList,
                                                K startDate,
                                                K endDate);
}
