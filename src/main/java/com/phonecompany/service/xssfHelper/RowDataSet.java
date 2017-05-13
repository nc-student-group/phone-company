package com.phonecompany.service.xssfHelper;

import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;

public final class RowDataSet {

    private final String rowName;
    private final List<Pair<Object, Object>> rowValues = new ArrayList<>();

    RowDataSet(String rowName) {
        this.rowName = rowName;
    }

    public String getRowName() {
        return rowName;
    }

    public List<Pair<Object, Object>> getRowValues() {
        return rowValues;
    }

    public RowDataSet addKeyValuePair(Object key, Object value) {
        rowValues.add(Pair.with(key, value));
        return this;
    }

    @Override
    public String toString() {
        return "RowDataSet{" +
                "rowName='" + rowName + '\'' +
                ", rowValues=" + rowValues +
                '}';
    }
}
