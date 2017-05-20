package com.phonecompany.service;

import com.phonecompany.annotations.ServiceStereotype;
import com.phonecompany.dao.interfaces.ComplaintDao;
import com.phonecompany.model.*;
import com.phonecompany.model.enums.ComplaintCategory;
import com.phonecompany.model.enums.ComplaintStatus;
import com.phonecompany.model.enums.WeekOfMonth;
import com.phonecompany.service.interfaces.ComplaintService;
import com.phonecompany.service.interfaces.StatisticsService;
import com.phonecompany.service.interfaces.UserService;
import com.phonecompany.service.xssfHelper.SheetDataSet;
import com.phonecompany.service.xssfHelper.Statistics;
import com.phonecompany.util.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.*;

@ServiceStereotype
public class ComplaintServiceImpl extends CrudServiceImpl<Complaint>
        implements ComplaintService {

    private static final Logger LOG = LoggerFactory.getLogger(ComplaintServiceImpl.class);

    private ComplaintDao complaintDao;
    private UserService userService;
    private StatisticsService<LocalDate, Long> statisticsService;

    @Autowired
    public ComplaintServiceImpl(ComplaintDao complaintDao,
                                UserService userService,
                                StatisticsService<LocalDate, Long> statisticsService) {
        super(complaintDao);
        this.complaintDao = complaintDao;
        this.userService = userService;
        this.statisticsService = statisticsService;
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
        Object[] args = new Object[]{category, status, new Long(0), new Long(0)};
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
    public Complaint setStatusIntraprocess(Complaint complaint) {
        if (complaint.getStatus() == ComplaintStatus.ACCEPTED) {
            complaint.setStatus(ComplaintStatus.INTRAPROCESS);
            complaintDao.update(complaint);
            LOG.debug("Complaint status {}", complaint.getStatus());
        } else {
            complaint = null;
            LOG.debug("Complaint status wasn't changed.");
        }
        return complaint;
    }

    @Override
    public Complaint setStatusAccomplished(Complaint complaint, String comment) {
        if (complaint.getStatus() == ComplaintStatus.INTRAPROCESS) {
            complaint.setStatus(ComplaintStatus.ACCOMPLISHED);
            complaint.setComment(comment);
            complaintDao.update(complaint);
            LOG.debug("Complaint status {}", complaint.getStatus());
        } else {
            complaint = null;
            LOG.debug("Complaint status wasn't changed.");
        }
        return complaint;
    }

    @Override
    public List<Complaint> getAllComplaintsSearch(int page, int size, String email, String status, String category) {
        Query.Builder query = new Query.Builder("complaint inner join dbuser on complaint.user_id = dbuser.id");
        query.where();
        query.addLikeCondition("email", email);
        if (!status.equals("-")) {
            query.and().addCondition("complaint.status=?", status);
        }
        if (!category.equals("-")) {
            query.and().addCondition("complaint.type=?", category);
        }
        query.addPaging(page, size);
        return complaintDao.getAllComplaintsSearch(query.build());
    }

    @Override
    public int getCountSearch(int page, int size, String email, String status, String category) {
        Query.Builder query = new Query.Builder("complaint inner join dbuser on complaint.user_id = dbuser.id");
        query.where();
        query.addLikeCondition("dbuser.email", email);
        if (!status.equals("-")) {
            query.and().addCondition("complaint.status=?", status);
        }
        if (!category.equals("-")) {
            query.and().addCondition("complaint.type=?", category);
        }
        return complaintDao.getAllComplaintsSearch(query.build()).size();
    }

    @Override
    public WeeklyComplaintStatistics getComplaintStatistics() {

        EnumMap<WeekOfMonth, Integer> numberOfCSComplaintsForTheLastMonth =
                this.complaintDao.getNumberOfComplaintsForTheLastMonthByCategory(ComplaintCategory.CUSTOMER_SERVICE);
        EnumMap<WeekOfMonth, Integer> numberOfSComplaintsForTheLastMonth =
                this.complaintDao.getNumberOfComplaintsForTheLastMonthByCategory(ComplaintCategory.SUGGESTION);
        EnumMap<WeekOfMonth, Integer> numberOfTSComplaintsForTheLastMonth =
                this.complaintDao.getNumberOfComplaintsForTheLastMonthByCategory(ComplaintCategory.TECHNICAL_SERVICE);

        return new WeeklyComplaintStatistics(numberOfCSComplaintsForTheLastMonth,
                numberOfSComplaintsForTheLastMonth,
                numberOfTSComplaintsForTheLastMonth);
    }

    @Override
    public SheetDataSet<LocalDate, Long> getComplaintStatisticsDataSet(long regionId,
                                                          LocalDate startDate,
                                                          LocalDate endDate) {

        List<Statistics> statisticsList = this.complaintDao
                .getComplaintStatisticsByRegionAndTimePeriod(regionId, startDate, endDate);

        return this.statisticsService
                .prepareStatisticsDataSet("Complaints", statisticsList,
                        startDate, endDate);
    }
}
