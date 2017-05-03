package com.phonecompany.service.interfaces;

import com.phonecompany.model.Complaint;
import com.phonecompany.model.enums.ComplaintCategory;

import java.util.List;
import java.util.Map;

public interface ComplaintService extends CrudService<Complaint> {
    public Complaint createComplaint(Complaint complaint);
    public List<ComplaintCategory> getAllComplaintCategory();
    public Map<String, Object> getComplaintsByCategory(String category, int page, int size);
}
