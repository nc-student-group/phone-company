package com.phonecompany.util;

import com.phonecompany.model.enums.interfaces.ItemType;
import com.phonecompany.service.interfaces.Statistics;

import java.time.LocalDate;
import java.util.function.Predicate;

/**
 * Class that provides with a set of filtering predicates
 *
 * @see com.phonecompany.service.xssfHelper.filterChain.DateFilter
 */
public class FilteringPredicates {

    /**
     * Makes a decision whether to filter out {@link Statistics} object depending
     * on the supplied {@code String} key.
     *
     * @param key key that defines a result of a filtering decision
     * @return instance of a {@code Predicate} that helps to filter out {@link Statistics}
     * objects by {@code String} keys
     * @see com.phonecompany.service.xssfHelper.filterChain.NamingFilter
     */
    public static Predicate<Statistics> getStatisticsByItemNamePredicate(String key) {
        return statistics -> statistics.getItemName().equals(key);
    }

    /**
     * Makes a decision whether to filter out {@link Statistics} object depending
     * on the supplied {@link ItemType} instance.
     *
     * @param itemType type that defines a result of a filtering decision
     * @return instance of a {@code Predicate} that helps to filter out {@link Statistics}
     * objects by {@code ItemType} instances
     * @see com.phonecompany.service.xssfHelper.filterChain.ItemTypeFilter
     */
    public static Predicate<Statistics> getStatisticsByItemTypePredicate(ItemType itemType) {
        return statistics -> statistics.getItemType().equals(itemType);
    }

    /**
     * Makes a decision whether to filter out {@link Statistics} object depending
     * on the supplied {@code LocalDate} object.
     *
     * @param date date object that defines a result of a filtering decision
     * @return instance of a {@code Predicate} that helps to filter out {@link Statistics}
     * objects by {@code LocalDate} instances
     * @see com.phonecompany.service.xssfHelper.filterChain.DateFilter
     */
    public static Predicate<Statistics> getStatisticsByLocalDatePredicate(LocalDate date) {
        return statistics -> statistics.getTimePoint().equals(date);
    }
}
