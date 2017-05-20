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
public final class RowDataSet<K, V> {

    private final String rowName;
    private final List<Pair<K, V>> rowValues = new ArrayList<>();

    RowDataSet(String rowName) {
        this.rowName = rowName;
    }

    public String getRowName() {
        return rowName;
    }

    public List<Pair<K, V>> getRowValues() {
        return rowValues;
    }

    public RowDataSet addKeyValuePair(K key, V value) {
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
