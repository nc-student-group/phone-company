package com.phonecompany.service.xssfHelper.filterChain;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Base class for all the implementations that provide filtering capabilities.
 *
 * @param <T> type of items in the list that is to be filtered
 * @param <K> type of key used to filter out items from the target list
 */
public abstract class Filter<T, K> {

    Filter<T, ?> successor; //the next filter in the chain
    K filteringKey;

    public Filter(K filteringKey) {
        this.filteringKey = filteringKey;
    }

    public void setSuccessor(Filter<T, ?> successor) {
        this.successor = successor;
    }

    /**
     * Triggers filtering chain.
     *
     * @param targetList list to be filtered
     * @return list filtered by the whole filter chain sequence
     */
    public abstract List<T> doFilter(List<T> targetList);

    /**
     * Filters an incoming list.
     *
     * @param statisticsList     target list to be filtered
     * @param filteringPredicate instance of {@code Predicate} which is used to
     *                           filter out objects from the target list
     * @return list filtered by a single filter chain unit
     */
    List<T> filterOutTargetList(List<T> statisticsList,
                                Predicate<T> filteringPredicate) {
        return statisticsList
                .stream()
                .filter(filteringPredicate)
                .collect(Collectors.toList());
    }
}
