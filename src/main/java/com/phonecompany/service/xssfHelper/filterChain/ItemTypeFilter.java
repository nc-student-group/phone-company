package com.phonecompany.service.xssfHelper.filterChain;

import com.phonecompany.service.xssfHelper.Statistics;
import com.phonecompany.model.enums.ItemType;

import java.util.List;
import java.util.function.Predicate;

import static com.phonecompany.util.TypeMapper.getStatisticsByItemTypePredicate;

public class ItemTypeFilter extends Filter<ItemType> {

    public ItemTypeFilter(ItemType filteringKey) {
        super(filteringKey);
    }

    @Override
    public List<Statistics> doFilter(List<Statistics> statisticsList) {
        Predicate<Statistics> itemTypePredicate =
                getStatisticsByItemTypePredicate(filteringKey);
        List<Statistics> filteredStatistics = this
                .filterOutStatistics(statisticsList, itemTypePredicate);
        return successor.doFilter(filteredStatistics);
    }
}
