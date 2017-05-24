package com.phonecompany.service.xssfHelper.filterChain;

import com.phonecompany.service.interfaces.Statistics;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

import static com.phonecompany.util.FilteringPredicates.getStatisticsByLocalDatePredicate;

/**
 * {@inheritDoc}
 */
public class DateFilter extends Filter<Statistics, LocalDate> {

    public DateFilter(LocalDate filteringKey) {
        super(filteringKey);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Statistics> doFilter(List<Statistics> targetList) {
        Predicate<Statistics> orderDatePredicate =
                getStatisticsByLocalDatePredicate(filteringKey);
        return this.filterOutTargetList(targetList, orderDatePredicate);
    }
}
