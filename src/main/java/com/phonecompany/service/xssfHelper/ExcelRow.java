package com.phonecompany.service.xssfHelper;

import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;

public class ExcelRow {

    private String rowName;
    private List<Pair<Object, Object>> rowValues = new ArrayList<>();

    ExcelRow(String rowName) {
        this.rowName = rowName;
    }

    public ExcelRow addKeyValuePair(Object key, Object value) {
        rowValues.add(Pair.with(key, value));
        return this;
    }

    @Override
    public String toString() {
        return "ExcelRow{" +
                "rowName='" + rowName + '\'' +
                ", rowValues=" + rowValues +
                '}';
    }
}
