package com.phonecompany.service.xssfHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Class which serves an object representation of the data that can be
 * contained within a single xls table
 *
 * <p>Consists of {@link RowDataSet} objects</p>
 *
 * @param <K> type of the objects that represent range of values
 *            (type of the values contained within an xls cell)
 * @param <V> type of the objects that represent range of definition
 *            (type of the values contained within an xls column header)
 */
public final class TableDataSet<K, V> {

    private final String tableName;
    private final List<RowDataSet<K, V>> rowDataSets = new ArrayList<>();

    TableDataSet(String tableName) {
        this.tableName = tableName;
    }

    public String getTableDataSetName() {
        return tableName;
    }

    public List<RowDataSet<K, V>> getRowDataSets() {
        return rowDataSets;
    }

    public RowDataSet<K, V> createRow(String rowName) {
        RowDataSet<K, V> rowDataSet = new RowDataSet<>(rowName);
        rowDataSets.add(rowDataSet);
        return rowDataSet;
    }

    @Override
    public String toString() {
        return "TableDataSet{" +
                "tableName='" + tableName + '\'' +
                ", rowDataSets=" + rowDataSets +
                '}';
    }
}
