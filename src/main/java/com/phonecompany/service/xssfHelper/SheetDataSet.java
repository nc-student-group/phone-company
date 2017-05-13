package com.phonecompany.service.xssfHelper;

import java.util.ArrayList;
import java.util.List;

public final class SheetDataSet {

    private final String sheetName;
    private final List<TableDataSet> excelTables = new ArrayList<>();

    public SheetDataSet(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getSheetName() {
        return sheetName;
    }

    public List<TableDataSet> getExcelTables() {
        return excelTables;
    }

    public TableDataSet createTable(String tableName) {
        TableDataSet excelTable = new TableDataSet(tableName);
        excelTables.add(excelTable);
        return excelTable;
    }

    @Override
    public String toString() {
        return "SheetDataSet{" +
                "sheetName='" + sheetName + '\'' +
                ", excelTables=" + excelTables +
                '}';
    }
}
