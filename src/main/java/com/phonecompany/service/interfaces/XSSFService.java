package com.phonecompany.service.interfaces;

import com.phonecompany.service.xssfHelper.BookDataSet;
import com.phonecompany.service.xssfHelper.SheetDataSet;

import java.io.InputStream;

public interface XSSFService<K, V> {
    InputStream generateReport(BookDataSet<K, V> book);
}
