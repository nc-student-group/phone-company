package com.phonecompany.service.xssfHelper.filterChain;

import com.phonecompany.model.OrderStatistics;
import com.phonecompany.model.enums.OrderType;

import java.util.List;
import java.util.function.Predicate;

import static com.phonecompany.util.TypeMapper.getStatisticsByOrderTypePredicate;

public class OrderTypeFilter extends Filter<OrderType> {

    public OrderTypeFilter(OrderType filteringKey) {
        super(filteringKey);
    }

    @Override
    public List<OrderStatistics> doFilter(List<OrderStatistics> statisticsList) {
        Predicate<OrderStatistics> targetNamePredicate =
                getStatisticsByOrderTypePredicate(filteringKey);
        List<OrderStatistics> filteredStatistics = this
                .filterOutStatistics(statisticsList, targetNamePredicate);
        return successor.doFilter(filteredStatistics);
    }
}
