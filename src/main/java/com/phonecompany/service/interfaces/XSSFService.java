package com.phonecompany.service.interfaces;

import com.phonecompany.model.Order;
import com.phonecompany.service.xssfHelper.ExcelSheet;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface XSSFService {
    void generateReport(Map<String, List<Order>> ordersMap,
                        List<LocalDate> timeLine);
}
