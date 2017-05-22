package com.phonecompany.service;

import com.phonecompany.annotations.ServiceStereotype;
import com.phonecompany.dao.interfaces.OrderDao;
import com.phonecompany.exception.service_layer.InsufficientFilteringException;
import com.phonecompany.model.enums.interfaces.ItemType;
import com.phonecompany.service.interfaces.StatisticsService;
import com.phonecompany.service.interfaces.Statistics;
import com.phonecompany.service.xssfHelper.filterChain.DateFilter;
import com.phonecompany.service.xssfHelper.filterChain.Filter;
import com.phonecompany.service.xssfHelper.filterChain.ItemTypeFilter;
import com.phonecompany.service.xssfHelper.filterChain.NamingFilter;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ServiceStereotype
public class OrderStatisticsServiceImpl extends AbstractStatisticsServiceImpl<LocalDate, Long>
        implements StatisticsService<LocalDate, Long> {

    private OrderDao orderDao;

    @Autowired
    public OrderStatisticsServiceImpl(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public Filter<Statistics, ?> createFilterChain(String itemName, ItemType itemType,
                                                   LocalDate rangeOfDefinitionPoint) {
        Filter<Statistics, ItemType> orderTypeFilter = new ItemTypeFilter(itemType);
        Filter<Statistics, String> namingFilter = new NamingFilter(itemName);
        Filter<Statistics, LocalDate> dateFilter = new DateFilter(rangeOfDefinitionPoint);

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
