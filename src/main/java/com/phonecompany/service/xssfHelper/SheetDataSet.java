package com.phonecompany.service.xssfHelper;

import java.util.ArrayList;
import java.util.List;

public final class SheetDataSet {

    private final String sheetName;
    private final List<TableDataSet> tableDataSets = new ArrayList<>();

    public SheetDataSet(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getSheetName() {
        return sheetName;
    }

    public List<TableDataSet> getTableDataSets() {
        return tableDataSets;
    }

    public TableDataSet createTable(String tableName) {
        TableDataSet excelTable = new TableDataSet(tableName);
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
