package com.phonecompany.service.interfaces;

import com.phonecompany.model.Complaint;
import com.phonecompany.model.enums.ComplaintCategory;

import java.util.List;

public interface ComplaintService extends CrudService<Complaint> {
    public Complaint createComplaint(Complaint complaint);
    public List<ComplaintCategory> getAllComplaintCategory();
}
