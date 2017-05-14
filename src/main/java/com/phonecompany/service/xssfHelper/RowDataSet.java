package com.phonecompany.service.xssfHelper;

import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Class which serves an object representation of the data that can be
 * contained within a single xls row. An example usage of the data
 * represented by the following type:
 *
 *          ---------------------------
 *          | rowName | V | V | V | V |
 *          ---------------------------
 *          | rowName | V | V | V | V |
 *          ---------------------------
 *          |         | K | K | K | K |
 *          ---------------------------
 *
 *  V - value in a cell
 *  K - key that depicts correspondence between cell values column
 *      headers
 */
public final class RowDataSet<V, K> {

    private final String rowName;
    private final List<Pair<V, K>> rowValues = new ArrayList<>();

    RowDataSet(String rowName) {
        this.rowName = rowName;
    }

    public String getRowName() {
        return rowName;
    }

    public List<Pair<V, K>> getRowValues() {
        return rowValues;
    }

    public RowDataSet addKeyValuePair(V value, K key) {
        rowValues.add(Pair.with(value, key));
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
