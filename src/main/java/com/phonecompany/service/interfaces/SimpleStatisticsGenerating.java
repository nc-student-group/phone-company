package com.phonecompany.service.interfaces;

import com.phonecompany.service.xssfHelper.SheetDataSet;

public interface SimpleStatisticsGenerating<K, V> {
    SheetDataSet<K, V> prepareStatisticsReportDataSet(K startDate, K endDate);
}
