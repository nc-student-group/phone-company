package com.phonecompany.service.xssfHelper.filterChain;

import com.phonecompany.model.enums.interfaces.ItemType;
import com.phonecompany.service.interfaces.Statistics;

import java.util.List;
import java.util.function.Predicate;

import static com.phonecompany.util.FilteringPredicates.getStatisticsByItemTypePredicate;

/**
 * {@inheritDoc}
 */
public class ItemTypeFilter extends Filter<Statistics, ItemType> {

    public ItemTypeFilter(ItemType filteringKey) {
        super(filteringKey);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Statistics> doFilter(List<Statistics> targetList) {
        Predicate<Statistics> itemTypePredicate =
                getStatisticsByItemTypePredicate(filteringKey);
        List<Statistics> filteredStatistics = this
                .filterOutTargetList(targetList, itemTypePredicate);
        return successor.doFilter(filteredStatistics);
    }
}
