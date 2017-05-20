package com.phonecompany.dao.interfaces;

import com.phonecompany.model.Complaint;
import com.phonecompany.service.xssfHelper.Statistics;
import com.phonecompany.util.Query;

import java.time.LocalDate;
import java.util.List;

import com.phonecompany.model.enums.ComplaintCategory;
import com.phonecompany.model.enums.WeekOfMonth;

import java.util.EnumMap;

public interface ComplaintDao extends JdbcOperations<Complaint>,
        AbstractPageableDao<Complaint> {
    List<Complaint> getAllComplaintsSearch(Query query);

    EnumMap<WeekOfMonth, Integer> getNumberOfComplaintsForTheLastMonthByCategory(ComplaintCategory type);

    List<Complaint> getComplaintsByRegionId(Long regionId);

    List<Statistics> getComplaintStatisticsByRegionAndTimePeriod(long regionId,
                                                                 LocalDate startDate,
                                                                 LocalDate endDate);
}
