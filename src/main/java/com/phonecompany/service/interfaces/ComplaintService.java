package com.phonecompany.service.interfaces;

import com.phonecompany.model.Complaint;
import com.phonecompany.model.WeeklyComplaintStatistics;
import com.phonecompany.service.xssfHelper.SheetDataSet;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

//TODO: please, remove these public access identifiers
public interface ComplaintService extends CrudService<Complaint> {
    Complaint createComplaint(Complaint complaint);

    Complaint createComplaintByCsr(Complaint complaint);

    Map<String, Object> getComplaintsTable(int category, int status, String email, String fromStr, String toStr,
                                           int orderBy, String orderByType, Long responsible, Long user,
                                           int page, int size);

//    Map<String, Object> getComplaints(String category, String status, int page, int size);
//
//    Map<String, Object> getComplaintsByCustomer(int id, int page, int size);
//
//    Map<String, Object> getComplaintsByResponsible(long responsibleId, String category, int page, int size);

    Complaint setStatusIntraprocess(Complaint complaint);

    Complaint setStatusAccomplished(Complaint complaint, String comment);

    List<Complaint> getAllComplaintsSearch(int page, int size, String email, String status, String category);

    int getCountSearch(int page, int size, String email, String status, String category);

    WeeklyComplaintStatistics getComplaintStatistics();

    SheetDataSet<LocalDate, Long> getComplaintStatisticsDataSet(long regionId,
                                                                LocalDate startDate,
                                                                LocalDate endDate);
}
