package com.phonecompany.service.interfaces;

import com.phonecompany.service.xssfHelper.BookDataSet;
import com.phonecompany.service.xssfHelper.SheetDataSet;

import java.io.InputStream;

public interface XSSFService<K, V> {
    /**
     * Generates a report for the given {@link BookDataSet}
     *
     * @param book object that contains information required to
     *             create a report
     * @return {@code InputStream} of all bytes of the report
     * @see BookDataSet
     */
    InputStream generateReport(BookDataSet<K, V> book);
}
