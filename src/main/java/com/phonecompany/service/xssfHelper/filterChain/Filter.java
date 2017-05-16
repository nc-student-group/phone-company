package com.phonecompany.service.xssfHelper.filterChain;

import com.phonecompany.model.OrderStatistics;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class Filter {

    protected Filter successor;
    protected Object filteringKey;

    public Filter(Object filteringKey) {
        this.filteringKey = filteringKey;
    }

    public void setSuccessor(Filter successor) {
        this.successor = successor;
    }

    public abstract List<OrderStatistics> doFilter(List<OrderStatistics> statisticsList);

    protected List<OrderStatistics> filterOutStatistics(List<OrderStatistics> statisticsList,
                                                        Predicate<OrderStatistics> filteringPredicate) {
        return statisticsList
                .stream()
                .filter(filteringPredicate)
                .collect(Collectors.toList());
    }
}
