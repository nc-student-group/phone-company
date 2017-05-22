package com.phonecompany.service.xssfHelper.filterChain;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class Filter<T, K> {

    Filter<T, ?> successor;
    K filteringKey;

    public Filter(K filteringKey) {
        this.filteringKey = filteringKey;
    }

    public void setSuccessor(Filter<T, ?> successor) {
        this.successor = successor;
    }

    public abstract List<T> doFilter(List<T> targetList);

    List<T> filterOutTargetList(List<T> statisticsList,
                                Predicate<T> filteringPredicate) {
        return statisticsList
                .stream()
                .filter(filteringPredicate)
                .collect(Collectors.toList());
    }
}
