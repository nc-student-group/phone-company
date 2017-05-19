package com.phonecompany.service;

import com.phonecompany.model.enums.ItemType;
import com.phonecompany.service.xssfHelper.RowDataSet;
import com.phonecompany.service.xssfHelper.SheetDataSet;
import com.phonecompany.service.xssfHelper.Statistics;
import com.phonecompany.service.xssfHelper.TableDataSet;
import com.phonecompany.service.xssfHelper.filterChain.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractStatisticsServiceImpl<K, V>
        implements StatisticsService<K, V> {

    /**
     * Prepares dataset containing information .........??.........
     * <p>
     * <p>The resulting dataset can be used in {@link XSSFServiceImpl} in order
     * to create an xls document that depicts an information of the dataset</p>
     *
     * @param sheetName expected sheet name
     * @return constructed sheet dataset
     */
    @Override
    public SheetDataSet<K, V> prepareStatisticsDataSet(String sheetName,
                                                       List<Statistics> statisticsList,
                                                       K startDate, K endDate) {
        SheetDataSet<K, V> sheet = new SheetDataSet<>(sheetName);
        List<ItemType> itemTypes = this.getItemTypesFromStatistics(statisticsList);
        List<K> timeLine = this.generateTimeLine(startDate, endDate);
        for (ItemType itemType : itemTypes) {
            this.populateExcelTableDataSet(sheet, itemType, statisticsList, timeLine);
        }
        return sheet;
    }

    private List<ItemType> getItemTypesFromStatistics(List<Statistics> statisticsList) {
        return statisticsList.stream()
                .map(Statistics::getItemType)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Returns an ordered list of unique creation dates of the orders that {@code OrderStatistics}
     * objects were generated from.
     *
     * @return set of unique dates corresponding to the elements in the incoming list
     */
    public abstract List<K> generateTimeLine(K startDate, K endDate);

    /**
     * Populates {@link SheetDataSet} object with its components (e.g. {@link TableDataSet})
     *
     * @param sheet    sheet a corresponding table representation will be created on
     * @param itemType item type that is used to filter out {@code Statistics} objects
     * @param timeLine
     */
    private void populateExcelTableDataSet(SheetDataSet<K, V> sheet,
                                           ItemType itemType,
                                           List<Statistics> statisticsList,
                                           List<K> timeLine) {
        TableDataSet<K, V> table = sheet.createTable(itemType.toString());
        List<String> uniqueProductNames = this.extractUniqueValues(statisticsList, Statistics::getItemName);
        for (String itemName : uniqueProductNames) {
            RowDataSet<K, V> row = table.createRow(itemName);
            this.populateRowDataSet(row, statisticsList, itemName, itemType, timeLine);
        }
    }

    private List<String> extractUniqueValues(List<Statistics> statisticsList,
                                             Function<Statistics, String> mapper) {
        return statisticsList.stream()
                .map(mapper)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Populates {@code RowDataSet} object with the cell representations
     *
     * @param row            row to be populated with data
     * @param statisticsList source to fetch statistical data from
     * @param itemName       item name the given row corresponds to
     * @param itemType       item type the given row corresponds to
     * @param timeLine       a set of unique dates at which orders were made
     */
    private void populateRowDataSet(RowDataSet<K, V> row,
                                    List<Statistics> statisticsList,
                                    String itemName, ItemType itemType,
                                    List<K> timeLine) {

        for (K date : timeLine) {
            Filter<?> filterChainHead = this.createFilterChain(itemName, itemType, date);
            List<Statistics> filteredStatistics = filterChainHead.doFilter(statisticsList);
            V orderNumberByDate = this.getOrderNumber(filteredStatistics);
            row.addKeyValuePair(date, orderNumberByDate);
        }
    }

    public abstract Filter<?> createFilterChain(String itemName, ItemType itemType, K datePoint);

    /**
     * Gets the number of orders made at the specified date.
     *
     * @param statisticsList order list that number will be fetched from
     * @return order number
     */
    public abstract V getOrderNumber(List<Statistics> statisticsList);
}
