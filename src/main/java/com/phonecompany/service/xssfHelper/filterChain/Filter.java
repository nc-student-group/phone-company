package com.phonecompany.service.xssfHelper.filterChain;

import com.phonecompany.service.xssfHelper.Statistics;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class Filter<K> {

    Filter<?> successor;
    K filteringKey;

    public Filter(K filteringKey) {
        this.filteringKey = filteringKey;
    }

    public void setSuccessor(Filter<?> successor) {
        this.successor = successor;
    }

    public abstract List<Statistics> doFilter(List<Statistics> statisticsList);

    List<Statistics> filterOutStatistics(List<Statistics> statisticsList,
                                         Predicate<Statistics> filteringPredicate) {
        return statisticsList
                .stream()
                .filter(filteringPredicate)
                .collect(Collectors.toList());
    }
}
