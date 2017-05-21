package com.phonecompany.service;

import com.phonecompany.annotations.ServiceStereotype;
import com.phonecompany.exception.service_layer.InsufficientFilteringException;
import com.phonecompany.model.enums.interfaces.ItemType;
import com.phonecompany.service.interfaces.StatisticsService;
import com.phonecompany.service.xssfHelper.Statistics;
import com.phonecompany.service.xssfHelper.filterChain.DateFilter;
import com.phonecompany.service.xssfHelper.filterChain.Filter;
import com.phonecompany.service.xssfHelper.filterChain.ItemTypeFilter;
import com.phonecompany.service.xssfHelper.filterChain.NamingFilter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ServiceStereotype
public class OrderStatisticsServiceImpl extends AbstractStatisticsServiceImpl<LocalDate, Long>
        implements StatisticsService<LocalDate, Long> {

    @Override
    public Filter<?> createFilterChain(String itemName, ItemType itemType, LocalDate datePoint) {
        Filter<ItemType> orderTypeFilter = new ItemTypeFilter(itemType);
        Filter<String> namingFilter = new NamingFilter(itemName);
        Filter<LocalDate> dateFilter = new DateFilter(datePoint);

        orderTypeFilter.setSuccessor(namingFilter);
        namingFilter.setSuccessor(dateFilter);
        return orderTypeFilter;
    }

    @Override
    public Long getValue(List<Statistics> statisticsList) {
            this.validateStatisticsList(statisticsList);
            if (statisticsList.size() == 0) {
                return 0L;
            }
            return statisticsList.get(0).getValue();
    }

    private void validateStatisticsList(List<Statistics> statisticsList) {
        int statisticsListSize = statisticsList.size();
        if (statisticsListSize > 1 || statisticsListSize < 0) {
            throw new InsufficientFilteringException(statisticsList);
        }
    }

    @Override
    public List<LocalDate> getRangeOfDefinition(LocalDate rangeStart, LocalDate rangeEnd) {
        List<LocalDate> timeLine = new ArrayList<>();
        while (rangeStart.isBefore(rangeEnd)) {
            timeLine.add(rangeStart);
            rangeStart = rangeStart.plusDays(1);
        }
        timeLine.add(rangeStart);
        return timeLine;
    }
}
