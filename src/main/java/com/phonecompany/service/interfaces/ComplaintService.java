package com.phonecompany.service.interfaces;

import com.phonecompany.model.Complaint;

import java.util.List;
import java.util.Map;

public interface ComplaintService extends CrudService<Complaint> {
    public Complaint createComplaint(Complaint complaint);
    public Complaint createComplaintByCsr(Complaint complaint);
    public Map<String, Object> getComplaints(String category, String status, int page, int size);
    public Map<String, Object> getComplaintsByCustomer(int id, int page, int size);
    public Map<String, Object> getComplaintsByResponsible(long responsibleId, String category, int page, int size);
    public Complaint setStatusIntraprocess(Complaint complaint);
    public Complaint setStatusAccomplished(Complaint complaint, String comment);
    public List<Complaint> getAllComplaintsSearch(int page, int size, String email,String status,String category);
    public int getCountSearch(int page, int size,String email,String status,String category);
}
