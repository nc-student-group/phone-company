package com.phonecompany.service.interfaces;

import com.phonecompany.model.*;
import com.phonecompany.service.xssfHelper.*;

import java.util.List;
import java.time.LocalDate;
import java.util.Map;

//TODO: please, remove these public access identifiers
public interface ComplaintService extends CrudService<Complaint> {
    public Complaint createComplaint(Complaint complaint);

    public Complaint createComplaintByCsr(Complaint complaint);

    public Map<String, Object> getComplaints(String category, String status, int page, int size);

    public Map<String, Object> getComplaintsByCustomer(int id, int page, int size);

    public Map<String, Object> getComplaintsByResponsible(long responsibleId, String category, int page, int size);

    public Complaint setStatusIntraprocess(Complaint complaint);

    public Complaint setStatusAccomplished(Complaint complaint, String comment);

    public Map<String, Object> getAllComplaintsSearch(int page, int size, String email, String status, String category);

    public int getCountSearch(int page, int size, String email, String status, String category);

    public WeeklyComplaintStatistics getComplaintStatistics();

    SheetDataSet<LocalDate, Long> getComplaintStatisticsDataSet(long regionId,
                                                                LocalDate startDate,
                                                                LocalDate endDate);
}
