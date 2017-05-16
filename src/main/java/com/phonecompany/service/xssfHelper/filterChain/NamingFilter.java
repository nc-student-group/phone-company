package com.phonecompany.service.xssfHelper.filterChain;

import com.phonecompany.model.OrderStatistics;

import java.util.List;
import java.util.function.Predicate;

import static com.phonecompany.util.TypeMapper.getStatisticsByTargetNamePredicate;

public class NamingFilter extends Filter {

    public NamingFilter(Object filteringKey) {
        super(filteringKey);
    }

    @Override
    public List<OrderStatistics> doFilter(List<OrderStatistics> statisticsList) {
        Predicate<OrderStatistics> targetNamePredicate =
                getStatisticsByTargetNamePredicate(filteringKey.toString());
        List<OrderStatistics> filteredStatistics = this
                .filterOutStatistics(statisticsList, targetNamePredicate);
        return successor.doFilter(filteredStatistics);
    }
}
