package com.phonecompany.service.xssfHelper;

import java.util.ArrayList;
import java.util.List;

public final class TableDataSet {

    private final String tableName;
    private final List<RowDataSet> rowList = new ArrayList<>();

    TableDataSet(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public List<RowDataSet> getRowList() {
        return rowList;
    }

    public RowDataSet createRow(String rowName) {
        RowDataSet excelRow = new RowDataSet(rowName);
        rowList.add(excelRow);
        return excelRow;
    }

    @Override
    public String toString() {
        return "TableDataSet{" +
                "tableName='" + tableName + '\'' +
                ", rowList=" + rowList +
                '}';
    }
}
