package com.phonecompany.service.xssfHelper.filterChain;

import com.phonecompany.model.OrderStatistics;

import java.util.List;
import java.util.function.Predicate;

import static com.phonecompany.util.TypeMapper.getStatisticsByTargetNamePredicate;

public class NamingFilter extends Filter<String> {

    public NamingFilter(String filteringKey) {
        super(filteringKey);
    }

    @Override
    public List<OrderStatistics> doFilter(List<OrderStatistics> statisticsList) {
        Predicate<OrderStatistics> targetNamePredicate =
                getStatisticsByTargetNamePredicate(filteringKey);
        List<OrderStatistics> filteredStatistics = this
                .filterOutStatistics(statisticsList, targetNamePredicate);
        return successor.doFilter(filteredStatistics);
    }
}
