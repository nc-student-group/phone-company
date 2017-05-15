package com.phonecompany.service.interfaces;

import com.phonecompany.service.xssfHelper.SheetDataSet;

public interface XSSFService<K, V> {
    void generateReport(SheetDataSet<K, V> excelSheet);
}
