package com.phonecompany.service.xssfHelper.filterChain;

import com.phonecompany.model.OrderStatistics;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

import static com.phonecompany.util.TypeMapper.getStatisticsByLocalDatePredicate;

public class DateFilter extends Filter<LocalDate> {

    public DateFilter(LocalDate filteringKey) {
        super(filteringKey);
    }

    @Override
    public List<OrderStatistics> doFilter(List<OrderStatistics> statisticsList) {
        Predicate<OrderStatistics> orderDatePredicate =
                getStatisticsByLocalDatePredicate(filteringKey);
        return this.filterOutStatistics(statisticsList, orderDatePredicate);
    }
}
