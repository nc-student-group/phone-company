package com.phonecompany.service.interfaces;

import com.phonecompany.model.Complaint;
import com.phonecompany.model.ComplaintStatistics;
import com.phonecompany.service.xssfHelper.SheetDataSet;

import java.time.LocalDate;
import java.util.Map;

public interface ComplaintService extends CrudService<Complaint> {
    public Complaint createComplaint(Complaint complaint);
    public Complaint createComplaintByCsr(Complaint complaint);
    public Map<String, Object> getComplaints(String category, String status, int page, int size);
    public Map<String, Object> getComplaintsByCustomer(int id, int page, int size);
    public Map<String, Object> getComplaintsByResponsible(long responsibleId, String category, int page, int size);
    public Complaint setStatusIntraprocess(Complaint complaint);
    public Complaint setStatusAccomplished(Complaint complaint, String comment);
    public ComplaintStatistics getComplaintStatistics();
    public SheetDataSet prepareComplaintReportDataSet(long regionId, LocalDate startDate, LocalDate endDate);
}
