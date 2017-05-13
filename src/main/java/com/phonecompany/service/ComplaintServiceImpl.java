package com.phonecompany.service;

import com.phonecompany.dao.interfaces.ComplaintDao;
import com.phonecompany.model.Complaint;
import com.phonecompany.model.User;
import com.phonecompany.model.enums.ComplaintStatus;
import com.phonecompany.service.interfaces.ComplaintService;
import com.phonecompany.service.interfaces.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ComplaintServiceImpl extends CrudServiceImpl<Complaint>
        implements ComplaintService{

    private static final Logger LOG = LoggerFactory.getLogger(ComplaintServiceImpl.class);

    private ComplaintDao complaintDao;
    private UserService userService;

    @Autowired
    public ComplaintServiceImpl(ComplaintDao complaintDao,
                                UserService userService){
        super(complaintDao);
        this.complaintDao = complaintDao;
        this.userService = userService;
    }

    @Override
    public Complaint createComplaint(Complaint complaint) {
        complaint.setStatus(ComplaintStatus.ACCEPTED);
        complaint.setDate(LocalDate.now());
        complaintDao.save(complaint);
        LOG.debug("Complaint added {}", complaint);

        return complaint;
    }

    @Override
    public Complaint createComplaintByCsr(Complaint complaint) {
        User persistedUser = userService.findByEmail(complaint.getUser().getEmail());
        if (persistedUser != null) {
            complaint.setUser(persistedUser);
            complaint.setStatus(ComplaintStatus.ACCEPTED);
            complaint.setDate(LocalDate.now());
            complaintDao.save(complaint);
            LOG.debug("Complaint added {}", complaint);
        } else {
            LOG.info("User with email " + complaint.getUser().getEmail() + " not found!");
            complaint.setUser(null);
        }

        return complaint;
    }

    @Override
    public Map<String, Object> getComplaints(String category, String status, int page, int size) {
        Map<String, Object> response = new HashMap<>();
        Object[] args = new Object[]{category, status,  new Long(0), new Long(0)};
        List<Complaint> complaints = this.complaintDao.getPaging(page, size, args);

        LOG.debug("Fetched complaints: {}", complaints);
        response.put("complaints", complaints);
        response.put("complaintsCount", this.complaintDao.getEntityCount(args));
        return response;
    }

    @Override
    public Map<String, Object> getComplaintsByCustomer(int id, int page, int size) {
        Map<String, Object> response = new HashMap<>();
        Object[] args = new Object[]{"-", "-", new Long(id), new Long(0)};
        List<Complaint> complaints = this.complaintDao.getPaging(page, size, args);

        LOG.debug("Fetched complaints: {}", complaints);
        response.put("complaints", complaints);
        response.put("complaintsCount", this.complaintDao.getEntityCount(args));
        return response;
    }

    @Override
    public Map<String, Object> getComplaintsByResponsible(long responsibleId, String category, int page, int size) {
        Map<String, Object> response = new HashMap<>();
        Object[] args = new Object[]{category, ComplaintStatus.INTRAPROCESS.toString(), new Long(0), responsibleId};
        List<Complaint> complaints = this.complaintDao.getPaging(page, size, args);

        LOG.debug("Fetched complaints: {}", complaints);
        response.put("complaints", complaints);
        response.put("complaintsCount", this.complaintDao.getEntityCount(args));
        return response;
    }

    @Override
    public Complaint setStatusIntraprocess(Complaint complaint){
        if (complaint.getStatus() == ComplaintStatus.ACCEPTED) {
            complaint.setStatus(ComplaintStatus.INTRAPROCESS);
            complaintDao.update(complaint);
            LOG.debug("Complaint status {}", complaint.getStatus());
        }
        else {
            complaint = null;
            LOG.debug("Complaint status wasn't changed.");
        }
        return complaint;
    }

    @Override
    public Complaint setStatusAccomplished(Complaint complaint, String comment){
        if (complaint.getStatus() == ComplaintStatus.INTRAPROCESS) {
            complaint.setStatus(ComplaintStatus.ACCOMPLISHED);
            complaint.setComment(comment);
            complaintDao.update(complaint);
            LOG.debug("Complaint status {}", complaint.getStatus());
        }
        else {
            complaint = null;
            LOG.debug("Complaint status wasn't changed.");
        }
        return complaint;
    }

}
