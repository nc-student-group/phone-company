package com.phonecompany.dao.interfaces;

import com.phonecompany.model.Complaint;
import com.phonecompany.util.Query;

import java.util.List;
import com.phonecompany.model.enums.ComplaintCategory;
import com.phonecompany.model.enums.WeekOfMonth;

import java.util.EnumMap;
import java.util.List;

public interface ComplaintDao extends CrudDao<Complaint>,
        AbstractPageableDao<Complaint> {
    public List<Complaint> getAllComplaintsSearch(Query query);
    public EnumMap<WeekOfMonth, Integer> getNumberOfComplaintsForTheLastMonthByCategory(ComplaintCategory type);
    public List<Complaint> getComplaintsByRegionId(Long regionId);
}
