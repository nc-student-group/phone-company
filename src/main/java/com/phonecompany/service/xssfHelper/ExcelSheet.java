package com.phonecompany.service.xssfHelper;

import java.util.ArrayList;
import java.util.List;

public class ExcelSheet {

    private String sheetName;
    private List<ExcelTable> excelTables = new ArrayList<>();

    public ExcelSheet(String sheetName) {
        this.sheetName = sheetName;
    }

    public ExcelTable createTable(String tableName) {
        ExcelTable excelTable = new ExcelTable(tableName);
        excelTables.add(excelTable);
        return excelTable;
    }

    @Override
    public String toString() {
        return "ExcelSheet{" +
                "sheetName='" + sheetName + '\'' +
                ", excelTables=" + excelTables +
                '}';
    }
}
