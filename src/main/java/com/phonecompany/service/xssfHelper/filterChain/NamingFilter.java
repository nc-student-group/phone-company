package com.phonecompany.service.xssfHelper.filterChain;

import com.phonecompany.service.interfaces.Statistics;

import java.util.List;
import java.util.function.Predicate;

import static com.phonecompany.util.FilteringPredicates.getStatisticsByItemNamePredicate;

/**
 * {@inheritDoc}
 */
public class NamingFilter extends Filter<Statistics, String> {

    public NamingFilter(String filteringKey) {
        super(filteringKey);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Statistics> doFilter(List<Statistics> targetList) {
        Predicate<Statistics> targetNamePredicate =
                getStatisticsByItemNamePredicate(filteringKey);
        List<Statistics> filteredStatistics = this
                .filterOutTargetList(targetList, targetNamePredicate);
        return successor.doFilter(filteredStatistics);
    }
}
