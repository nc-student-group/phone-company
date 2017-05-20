package com.phonecompany.service.xssfHelper.filterChain;

import com.phonecompany.service.xssfHelper.Statistics;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

import static com.phonecompany.util.TypeMapper.getStatisticsByLocalDatePredicate;

public class DateFilter extends Filter<LocalDate> {

    public DateFilter(LocalDate filteringKey) {
        super(filteringKey);
    }

    @Override
    public List<Statistics> doFilter(List<Statistics> statisticsList) {
        Predicate<Statistics> orderDatePredicate =
                getStatisticsByLocalDatePredicate(filteringKey);
        return this.filterOutStatistics(statisticsList, orderDatePredicate);
    }
}
