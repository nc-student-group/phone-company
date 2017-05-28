package com.phonecompany.service;

import com.phonecompany.model.enums.interfaces.ItemType;
import com.phonecompany.service.interfaces.Statistics;
import com.phonecompany.service.interfaces.StatisticsService;
import com.phonecompany.service.xssfHelper.RowDataSet;
import com.phonecompany.service.xssfHelper.SheetDataSet;
import com.phonecompany.service.xssfHelper.TableDataSet;
import com.phonecompany.service.xssfHelper.filterChain.Filter;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * {@inheritDoc}
 */
public abstract class AbstractStatisticsServiceImpl<K, V>
        implements StatisticsService<K, V> {

    /**
     * {@inheritDoc}
     */
    @Override
    public SheetDataSet<K, V> prepareStatisticsDataSet(String sheetName,
                                                       List<Statistics> statisticsList,
                                                       K startOfRange, K endOfRange) {
        SheetDataSet<K, V> sheet = new SheetDataSet<>(sheetName);
        List<ItemType> itemTypes = this.getItemTypesFromStatistics(statisticsList);
        List<K> rangeOfDefinition = this.getRangeOfDefinition(startOfRange, endOfRange);
        for (ItemType itemType : itemTypes) {
            this.populateTableDataSet(sheet, itemType, statisticsList, rangeOfDefinition);
        }
        return sheet;
    }

    /**
     * Retrieves all the distinct item types contained among objects that represent
     * statistical information.
     *
     * @param statisticsList list of the statistical data that item types will be
     *                       retrieved from
     * @return list of distinct item types
     */
    private List<ItemType> getItemTypesFromStatistics(List<Statistics> statisticsList) {
        return statisticsList.stream()
                .map(Statistics::getItemType)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Prepares a set of values which will serve as a range of definition for the
     * values contained within the constructed {@link SheetDataSet}.
     *
     * @param startOfRange start of the definition range
     * @param endOfRange   end of the definition range
     * @return range of definition represented as {@code List}
     */
    public abstract List<K> getRangeOfDefinition(K startOfRange, K endOfRange);

    /**
     * Populates {@link SheetDataSet} object with its components (with {@link TableDataSet}s).
     *
     * @param sheet           sheet that a corresponding table representation will be created on
     * @param itemType        item type that is used to filter out {@code Statistics} objects
     * @param statisticsList  list that statistical data will be retrieved from and placed into
     *                        the corresponding {@link TableDataSet}s
     * @param definitionRange range of definition for the values from the table
     */
    private void populateTableDataSet(SheetDataSet<K, V> sheet, ItemType itemType,
                                      List<Statistics> statisticsList, List<K> definitionRange) {
        TableDataSet<K, V> table = sheet.createTable(itemType.toString());
        List<String> uniqueProductNames = this.extractUniqueValues(statisticsList, Statistics::getItemName);
        for (String itemName : uniqueProductNames) {
            RowDataSet<K, V> row = table.createRow(itemName);
            FilteringConditions filteringConditions = new FilteringConditions(itemName, itemType, definitionRange);
            this.populateRowDataSet(row, filteringConditions, statisticsList);
        }
    }

    /**
     * Maps items from the list that represent statistical information to a set of
     * distinct values somehow related to the items from this list.
     *
     * @param statisticsList list that distinct values will be retrieved from
     * @param mapper         instance of {@code Function} used to perform mapping
     * @param <E>            type of objects contained in the resulting list
     * @return {@code List} filled with distinct values
     */
    private <E> List<E> extractUniqueValues(List<Statistics> statisticsList,
                                            Function<Statistics, E> mapper) {
        return statisticsList.stream()
                .map(mapper)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Populates {@code RowDataSet} object with the cell representations.
     *
     * @param row row to be populated with data
     */
    private void populateRowDataSet(RowDataSet<K, V> row, FilteringConditions filteringConditions,
                                    List<Statistics> statisticsList) {

        for (K rangePoint : filteringConditions.rangeOfDefinition) {
            Filter<Statistics, ?> filterChainHead = this
                    .createFilterChain(filteringConditions.itemName, filteringConditions.itemType, rangePoint);
            List<Statistics> filteredStatistics = filterChainHead.doFilter(statisticsList);
            V value = this.getValue(filteredStatistics);
            row.addKeyValuePair(rangePoint, value);
        }
    }

    /**
     * Constructs filter chain that will perform filtering upon the {@code List} of {@link Statistics}
     * objects.
     *
     * @param itemName   item <b>name</b> that will be used to create one of the filtering units
     * @param itemType   item <b>type</b> that will be used to create one of the filtering units
     * @param rangePoint single <b>point</b> on range of definition that will be used to create
     *                   one of the filtering units
     * @return filter head that filtering process will begin from
     */
    public abstract Filter<Statistics, ?> createFilterChain(String itemName, ItemType itemType,
                                                            K rangePoint);

    /**
     * Gets the value that serves as a numerical representation of the statistical data.
     * <p>
     * <p>For instance: number of orders or number of complaints</p>
     *
     * @param statisticsList list of statistical data that numerical representations will
     *                       be extracted from
     * @return numerical representation
     */
    public abstract V getValue(List<Statistics> statisticsList);

    private class FilteringConditions {

        //filtering conditions
        String itemName;
        ItemType itemType;
        List<K> rangeOfDefinition;

        /**
         * @param itemName          item name data should be filtered by
         * @param itemType          item type data should be filtered by
         * @param rangeOfDefinition a set of unique values that represent a range of
         *                          definition for the data to be filtered
         */
        FilteringConditions(String itemName, ItemType itemType,
                            List<K> rangeOfDefinition) {
            this.itemName = itemName;
            this.itemType = itemType;
            this.rangeOfDefinition = rangeOfDefinition;
        }
    }
}
