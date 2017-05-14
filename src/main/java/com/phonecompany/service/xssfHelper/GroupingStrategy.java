package com.phonecompany.service.xssfHelper;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Class that declares a set of methods that define a way of how objects
 * of type <V> would be be grouped together using a specific key of type <K>
 */
public abstract class GroupingStrategy<V, K> {

    /**
     * Provides with an instance of {@code Function} which is intended to
     * map objects of type <V> to the key that is somehow related to the
     * given object.
     * For example, it can provide the following mapping:
     * order -> name of the tariff this order was made for
     *
     * @return mapping function
     * @see TariffGroupingStrategy
     */
    public abstract Function<V, K> getValueToKeyMapper();

    /**
     * Provides with a condition that will be used to filter out objects
     * related only to the key supplied as a parameter.
     *
     * @param key key which is used to filter out objects
     * @return filtering condition represented as an instance of {@code Predicate}
     */
    public abstract Predicate<V> getFilteringCondition(K key);
}
