package com.phonecompany.service.interfaces;

import com.phonecompany.service.xssfHelper.SheetDataSet;

import java.util.List;

/**
 * Class that helps to rearrange statistical data representing it in a
 * form that could be easily parsed later on.
 *
 * @param <K> type of objects that represent range of definition of the
 *            statistical data
 * @param <V> type of objects that represent range of values of the
 *            statistical data
 */
public interface StatisticsService<K, V> {

    /**
     * Prepares data set containing statistical information rearranged in such way
     * that it can be easily parsed later on.
     *
     * @param sheetName      expected sheet name
     * @param statisticsList list containing statistical data which is to
     *                       be rearranged
     * @param startOfRange   start of the definition range of the statistical data
     * @param endOfRange     end of the definition range of the statistical data
     * @return constructed sheet data set {@link SheetDataSet}
     */
    SheetDataSet<K, V> prepareStatisticsDataSet(String sheetName,
                                                List<Statistics> statisticsList,
                                                K startOfRange,
                                                K endOfRange);
}
