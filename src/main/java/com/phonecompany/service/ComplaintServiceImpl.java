package com.phonecompany.service;

import com.phonecompany.dao.interfaces.ComplaintDao;
import com.phonecompany.model.Complaint;
import com.phonecompany.service.interfaces.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComplaintServiceImpl extends CrudServiceImpl<Complaint> implements ComplaintService{

    private ComplaintDao complaintDao;

    @Autowired
    public ComplaintServiceImpl(ComplaintDao complaintDao){
        super(complaintDao);
        this.complaintDao = complaintDao;
    }
}
