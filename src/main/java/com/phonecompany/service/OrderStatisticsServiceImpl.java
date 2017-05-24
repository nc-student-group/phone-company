package com.phonecompany.service;

import com.phonecompany.annotations.ServiceStereotype;
import com.phonecompany.exception.service_layer.InsufficientFiltrationException;
import com.phonecompany.model.enums.interfaces.ItemType;
import com.phonecompany.service.interfaces.StatisticsService;
import com.phonecompany.service.interfaces.Statistics;
import com.phonecompany.service.xssfHelper.filterChain.DateFilter;
import com.phonecompany.service.xssfHelper.filterChain.Filter;
import com.phonecompany.service.xssfHelper.filterChain.ItemTypeFilter;
import com.phonecompany.service.xssfHelper.filterChain.NamingFilter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * {@inheritDoc}
 */
@ServiceStereotype
public class OrderStatisticsServiceImpl extends AbstractStatisticsServiceImpl<LocalDate, Long>
        implements StatisticsService<LocalDate, Long> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Filter<Statistics, ?> createFilterChain(String itemName, ItemType itemType,
                                                   LocalDate rangePoint) {
        Filter<Statistics, ItemType> orderTypeFilter = new ItemTypeFilter(itemType);
        Filter<Statistics, String> namingFilter = new NamingFilter(itemName);
        Filter<Statistics, LocalDate> dateFilter = new DateFilter(rangePoint);

        orderTypeFilter.setSuccessor(namingFilter);
        namingFilter.setSuccessor(dateFilter);
        return orderTypeFilter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getValue(List<Statistics> statisticsList) {
        this.validateStatisticsList(statisticsList);
        if (statisticsList.size() == 0) {
            return 0L;
        }
        return statisticsList.get(0).getValue();
    }

    /**
     * Makes sure that the incoming list was filtered properly
     *
     * @param statisticsList list to validate
     * @throws InsufficientFiltrationException if list was not properly filtered
     */
    private void validateStatisticsList(List<Statistics> statisticsList) {
        int statisticsListSize = statisticsList.size();
        if (statisticsListSize > 1 || statisticsListSize < 0) {
            throw new InsufficientFiltrationException(statisticsList);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LocalDate> getRangeOfDefinition(LocalDate startOfRange, LocalDate endOfRange) {
        List<LocalDate> timeLine = new ArrayList<>();
        while (startOfRange.isBefore(endOfRange)) {
            timeLine.add(startOfRange);
            startOfRange = startOfRange.plusDays(1);
        }
        timeLine.add(startOfRange);
        return timeLine;
    }
}
