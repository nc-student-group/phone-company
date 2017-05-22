package com.phonecompany.service.interfaces;

import com.phonecompany.service.xssfHelper.SheetDataSet;

import java.util.List;

public interface StatisticsService<K, V> {
    SheetDataSet<K, V> prepareStatisticsDataSet(String sheetName,
                                                List<Statistics> statisticsList,
                                                K startDate,
                                                K endDate);
}
