package com.phonecompany.service.xssfHelper.filterChain;

import com.phonecompany.service.xssfHelper.Statistics;

import java.util.List;
import java.util.function.Predicate;

import static com.phonecompany.util.TypeMapper.getStatisticsByItemNamePredicate;

public class NamingFilter extends Filter<String> {

    public NamingFilter(String filteringKey) {
        super(filteringKey);
    }

    @Override
    public List<Statistics> doFilter(List<Statistics> statisticsList) {
        Predicate<Statistics> targetNamePredicate =
                getStatisticsByItemNamePredicate(filteringKey);
        List<Statistics> filteredStatistics = this
                .filterOutStatistics(statisticsList, targetNamePredicate);
        return successor.doFilter(filteredStatistics);
    }
}
