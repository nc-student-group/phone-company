package com.phonecompany.service.xssfHelper;

import java.util.ArrayList;
import java.util.List;

public final class TableDataSet {

    private final String tableName;
    private final List<RowDataSet> rowDataSets = new ArrayList<>();

    TableDataSet(String tableName) {
        this.tableName = tableName;
    }

    public String getTableDataSetName() {
        return tableName;
    }

    public List<RowDataSet> getRowDataSets() {
        return rowDataSets;
    }

    public RowDataSet createRow(String rowName) {
        RowDataSet rowDataSet = new RowDataSet(rowName);
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
