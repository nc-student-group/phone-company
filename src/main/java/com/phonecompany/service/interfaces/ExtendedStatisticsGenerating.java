package com.phonecompany.service.interfaces;

import com.phonecompany.service.xssfHelper.SheetDataSet;

public interface ExtendedStatisticsGenerating<K, V> {
    SheetDataSet<K, V>
    getTariffStatisticsDataSet(long identifier,
                                                  K startingCondition,
                                                  K endingCondition);
}
