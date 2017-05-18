package com.phonecompany.service;

import com.phonecompany.exception.InsufficientFilteringException;
import com.phonecompany.service.xssfHelper.Statistics;
import com.phonecompany.model.enums.ItemType;
import com.phonecompany.service.xssfHelper.RowDataSet;
import com.phonecompany.service.xssfHelper.SheetDataSet;
import com.phonecompany.service.xssfHelper.TableDataSet;
import com.phonecompany.service.xssfHelper.filterChain.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractStatisticsServiceImpl implements StatisticsService {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractStatisticsServiceImpl.class);

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
    public SheetDataSet<LocalDate, Long> prepareStatisticsDataSet(String sheetName,
                                                                  List<Statistics> statisticsList,
                                                                  LocalDate startDate,
                                                                  LocalDate endDate) {
        SheetDataSet<LocalDate, Long> sheet = new SheetDataSet<>(sheetName);
        List<ItemType> itemTypes = this.getItemTypesFromStatistics(statisticsList);
        List<LocalDate> timeLine = this.generateTimeLine(startDate, endDate);
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
    private List<LocalDate> generateTimeLine(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> timeLine = new ArrayList<>();
        while (startDate.isBefore(endDate)) {
            timeLine.add(startDate);
            startDate = startDate.plusDays(1);
        }
        timeLine.add(startDate);
        return timeLine;
    }

    /**
     * Populates {@link SheetDataSet} object with its components (e.g. {@link TableDataSet})
     *
     * @param sheet    sheet a corresponding table representation will be created on
     * @param itemType item type that is used to filter out {@code Statistics} objects
     * @param timeLine
     */
    private void populateExcelTableDataSet(SheetDataSet<LocalDate, Long> sheet,
                                           ItemType itemType,
                                           List<Statistics> statisticsList,
                                           List<LocalDate> timeLine) {
        TableDataSet<LocalDate, Long> table = sheet.createTable(itemType.toString());
        List<String> uniqueProductNames = this.extractUniqueValues(statisticsList, Statistics::getItemName);
        for (String itemName : uniqueProductNames) {
            RowDataSet<LocalDate, Long> row = table.createRow(itemName);
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
    private void populateRowDataSet(RowDataSet<LocalDate, Long> row,
                                    List<Statistics> statisticsList,
                                    String itemName, ItemType itemType,
                                    List<LocalDate> timeLine) {

        for (LocalDate date : timeLine) {
            Filter<?> filterChainHead = this.createFilterChain(itemName, itemType, date);
            List<Statistics> filteredStatistics = filterChainHead.doFilter(statisticsList);
            long orderNumberByDate = this.getOrderNumber(filteredStatistics);
            row.addKeyValuePair(date, orderNumberByDate);
        }
    }

    public abstract Filter<?> createFilterChain(String itemName, ItemType itemType, LocalDate datePoint);

    /**
     * Gets the number of orders made at the specified date.
     *
     * @param statisticsList order list that number will be fetched from
     * @return order number
     */
    private Long getOrderNumber(List<Statistics> statisticsList) {
        this.validateStatisticsList(statisticsList);
        if (statisticsList.size() == 0) {
            return 0L;
        }
        return statisticsList.get(0).getCount();
    }

    private void validateStatisticsList(List<Statistics> statisticsList) {
        int statisticsListSize = statisticsList.size();
        if (statisticsListSize > 1 || statisticsListSize < 0) {
            throw new InsufficientFilteringException(statisticsList);
        }
    }
}
