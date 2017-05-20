package com.phonecompany.service.interfaces;

import com.phonecompany.service.xssfHelper.SheetDataSet;
import com.phonecompany.service.xssfHelper.Statistics;

import java.time.LocalDate;
import java.util.List;

public interface ExtendedStatisticsGenerating<K, V> {
    SheetDataSet<K, V> getTariffStatisticsDataSet(long identifier,
                                                  K startingCondition,
                                                  K endingCondition);
}
