package com.phonecompany.service.interfaces;

import com.phonecompany.service.xssfHelper.SheetDataSet;

import java.io.InputStream;

public interface XSSFService<K, V> {
    InputStream generateReport(SheetDataSet<K, V> excelSheet);
}
