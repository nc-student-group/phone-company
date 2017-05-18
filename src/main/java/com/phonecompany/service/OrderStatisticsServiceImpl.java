package com.phonecompany.service;

import com.phonecompany.annotations.ServiceStereotype;
import com.phonecompany.model.enums.ItemType;
import com.phonecompany.service.xssfHelper.filterChain.DateFilter;
import com.phonecompany.service.xssfHelper.filterChain.Filter;
import com.phonecompany.service.xssfHelper.filterChain.ItemTypeFilter;
import com.phonecompany.service.xssfHelper.filterChain.NamingFilter;

import java.time.LocalDate;

@ServiceStereotype
public class OrderStatisticsServiceImpl extends AbstractStatisticsServiceImpl
        implements StatisticsService {

    @Override
    public Filter<?> createFilterChain(String itemName, ItemType itemType, LocalDate datePoint) {
        Filter<ItemType> orderTypeFilter = new ItemTypeFilter(itemType);
        Filter<String> namingFilter = new NamingFilter(itemName);
        Filter<LocalDate> dateFilter = new DateFilter(datePoint);

        orderTypeFilter.setSuccessor(namingFilter);
        namingFilter.setSuccessor(dateFilter);
        return orderTypeFilter;
    }
}
