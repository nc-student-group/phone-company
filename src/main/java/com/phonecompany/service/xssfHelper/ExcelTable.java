package com.phonecompany.service.xssfHelper;

import java.util.ArrayList;
import java.util.List;

public class ExcelTable {

    private String tableName;
    private List<ExcelRow> rowList = new ArrayList<>();

    ExcelTable(String tableName) {
        this.tableName = tableName;
    }

    public ExcelRow createRow(String rowName) {
        ExcelRow excelRow = new ExcelRow(rowName);
        rowList.add(excelRow);
        return excelRow;
    }

    @Override
    public String toString() {
        return "ExcelTable{" +
                "tableName='" + tableName + '\'' +
                ", rowList=" + rowList +
                '}';
    }
}
