package com.phonecompany.service.xssfHelper;

import java.util.ArrayList;
import java.util.List;

public class BookDataSet<K, V> {

    private final List<SheetDataSet<K, V>> sheetDataSets = new ArrayList<>();

    public BookDataSet() {
    }

    public List<SheetDataSet<K, V>> getSheetDataSets() {
        return sheetDataSets;
    }

    public BookDataSet<K, V> addSheet(SheetDataSet<K, V> sheetDataSet) {
        sheetDataSets.add(sheetDataSet);
        return this;
    }

    @Override
    public String toString() {
        return "BookDataSet{" +
                ", sheetDataSets=" + sheetDataSets +
                '}';
    }
}
