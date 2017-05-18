package com.phonecompany.service.interfaces;

import com.phonecompany.service.xssfHelper.Statistics;

import java.util.List;

public interface ExtendedStatisticsGenerating<K> {
    List<Statistics> getTariffStatisticsData(long identifier,
                                             K startingCondition,
                                             K endingCondition);
}
