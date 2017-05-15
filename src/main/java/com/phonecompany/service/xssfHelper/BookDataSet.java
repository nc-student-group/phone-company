package com.phonecompany.service.xssfHelper;

import java.util.ArrayList;
import java.util.List;

public class BookDataSet<K, V> {

    private final String bookName;
    private final List<SheetDataSet<K, V>> sheetDataSets = new ArrayList<>();

    public BookDataSet(String bookName) {
        this.bookName = bookName;
    }

    public String getBookName() {
        return bookName;
    }

    public List<SheetDataSet<K, V>> getSheetDataSets() {
        return sheetDataSets;
    }

    public void addSheet(SheetDataSet<K, V> sheetDataSet) {
        sheetDataSets.add(sheetDataSet);
    }

    @Override
    public String toString() {
        return "BookDataSet{" +
                "bookName='" + bookName + '\'' +
                ", sheetDataSets=" + sheetDataSets +
                '}';
    }
}
