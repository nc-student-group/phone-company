package com.phonecompany.service.xssfHelper.filterChain;

import com.phonecompany.service.xssfHelper.Statistics;
import com.phonecompany.model.enums.ItemType;

import java.util.List;
import java.util.function.Predicate;

import static com.phonecompany.util.TypeMapper.getStatisticsByOrderTypePredicate;

public class ItemTypeFilter extends Filter<ItemType> {

    public ItemTypeFilter(ItemType filteringKey) {
        super(filteringKey);
    }

    @Override
    public List<Statistics> doFilter(List<Statistics> statisticsList) {
        Predicate<Statistics> targetNamePredicate =
                getStatisticsByOrderTypePredicate(filteringKey);
        List<Statistics> filteredStatistics = this
                .filterOutStatistics(statisticsList, targetNamePredicate);
        return successor.doFilter(filteredStatistics);
    }
}
