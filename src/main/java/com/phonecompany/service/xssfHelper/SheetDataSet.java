package com.phonecompany.service.xssfHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Class which serves an object representation of the data that can be
 * contained within a single xls sheet
 * <p>
 * <p>It can be used as an object that transfers processed data to the
 * service where this data will be rendered as an xls document</p>
 * <p>
 * <p>Consists of {@link TableDataSet} objects</p>
 *
 * @param <K> type of the objects that represent range of definition
 *            (type of the values contained within an xls column header)
 * @param <V> type of the objects that represent range of values
 *            (type of the values contained within an xls cell)
 */
public final class SheetDataSet<K, V> {

    private final String sheetName;
    private final List<TableDataSet<K, V>> tableDataSets = new ArrayList<>();

    public SheetDataSet(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getSheetName() {
        return sheetName;
    }

    public List<TableDataSet<K, V>> getTableDataSets() {
        return tableDataSets;
    }

    public TableDataSet<K, V> createTable(String tableName) {
        TableDataSet<K, V> excelTable = new TableDataSet<>(tableName);
        tableDataSets.add(excelTable);
        return excelTable;
    }

    @Override
    public String toString() {
        return "SheetDataSet{" +
                "sheetName='" + sheetName + '\'' +
                ", tableDataSets=" + tableDataSets +
                '}';
    }
}
