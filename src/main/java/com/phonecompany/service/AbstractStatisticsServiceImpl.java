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

public abstract class AbstractStatisticsServiceImpl<K, V>
        implements StatisticsService<K, V> {

    /**
     * Prepares data set containing statistical information rearranged in
     * such way that it can be easily parsed later on.
     *
     * @param sheetName      expected sheet name
     * @param statisticsList list containing statistical data which is to
     *                       be rearranged into {@link SheetDataSet} object
     * @param startOfRange   start of the time period that statistics was
     *                       generated for
     * @param endOfRange     end of the time period that statistics was
     *                       generated for
     * @return constructed sheet data set
     */
    @Override
    public SheetDataSet<K, V> prepareStatisticsDataSet(String sheetName,
                                                       List<Statistics> statisticsList,
                                                       K startOfRange, K endOfRange) {
        SheetDataSet<K, V> sheet = new SheetDataSet<>(sheetName);
        List<ItemType> itemTypes = this.getItemTypesFromStatistics(statisticsList);
        List<K> timeLine = this.getRangeOfDefinition(startOfRange, endOfRange);
        for (ItemType itemType : itemTypes) {
            this.populateTableDataSet(sheet, itemType, statisticsList, timeLine);
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
     * Populates {@link SheetDataSet} object with its components (with {@link TableDataSet}s)
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
            this.populateRowDataSet(row, statisticsList, itemName, itemType, definitionRange);
        }
    }

    /**
     * Maps items from the list that represent statistical information to a
     * set of distinct values somehow related to the items from this list.
     *
     * @param statisticsList list that distinct values will be retrieved from
     * @param mapper         instance of {@code Function} used to map
     * @param <E>
     * @return
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
     * @param row               row to be populated with data
     * @param statisticsList    source to fetch statistical data from
     * @param itemName          item name the given row corresponds to
     * @param itemType          item type the given row corresponds to
     * @param rangeOfDefinition a set of unique values that represent a
     *                          range of definition for the given row
     */
    private void populateRowDataSet(RowDataSet<K, V> row,
                                    List<Statistics> statisticsList,
                                    String itemName, ItemType itemType,
                                    List<K> rangeOfDefinition) {

        for (K rangePoint : rangeOfDefinition) {
            Filter<Statistics, ?> filterChainHead = this
                    .createFilterChain(itemName, itemType, rangePoint);
            List<Statistics> filteredStatistics = filterChainHead.doFilter(statisticsList);
            V value = this.getValue(filteredStatistics);
            row.addKeyValuePair(rangePoint, value);
        }
    }

    /**
     * Constructs an object that will perform filtering upon the {@code List} of
     * {@link Statistics} objects
     *
     * @param itemName               item name that will be used to create one of the filtering chains
     * @param itemType               item type that will be used to create one of the filtering chains
     * @param rangeOfDefinitionPoint range of definition point that will be used to create
     *                               one of the filtering chains
     * @return filter head that filtering process will begin from
     */
    public abstract Filter<Statistics, ?> createFilterChain(String itemName, ItemType itemType,
                                                            K rangeOfDefinitionPoint);

    /**
     * Gets the number of orders made at the specified date.
     *
     * @param statisticsList order list that number will be fetched from
     * @return order number
     */
    public abstract V getValue(List<Statistics> statisticsList);
}
