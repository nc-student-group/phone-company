package com.phonecompany.service;

import com.phonecompany.annotations.ServiceStereotype;
import com.phonecompany.dao.interfaces.ComplaintDao;
import com.phonecompany.exception.ConflictException;
import com.phonecompany.exception.EmptyResultSetException;
import com.phonecompany.model.Complaint;
import com.phonecompany.model.User;
import com.phonecompany.model.WeeklyComplaintStatistics;
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
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Map<String, Object> getComplaints(String category, String status, int page, int size, String partOfEmail,
                                             String dateFrom, String dateTo, String partOfSubject, int orderBy,
                                             String orderByType) {
        Query query = this.buildQuery(0, status, category, page, size, partOfEmail, dateFrom, dateTo,
                partOfSubject, orderBy, orderByType);
        List<Complaint> complaints = this.complaintDao.executeForList(query.getQuery(),
                query.getPreparedStatementParams().toArray());
        LOG.debug("Fetched complaints: {}", complaints);
        Map<String, Object> response = new HashMap<>();
        response.put("complaints", complaints);
        response.put("complaintsCount", this.complaintDao.executeForInt(query.getCountQuery(),
                query.getCountParams().toArray()));
        return response;
    }

    @Override
    public Map<String, Object> getComplaintsByCustomer(int id, int page, int size) {
        Query.Builder builder = new Query.Builder("complaint");
        builder.where().addCondition("user_id=?", id).addPaging(page, size);
        Query query = builder.build();
        List<Complaint> complaints = this.complaintDao.executeForList(query.getQuery(),
                query.getPreparedStatementParams().toArray());
        LOG.debug("Fetched complaints: {}", complaints);
        Map<String, Object> response = new HashMap<>();
        response.put("complaints", complaints);
        response.put("complaintsCount", this.complaintDao.executeForInt(query.getCountQuery(),
                query.getCountParams().toArray()));
        return response;
    }

    @Override
    public Map<String, Object> getComplaintsByResponsible(long responsibleId, String category, int page, int size,
                                                          String partOfEmail, String dateFrom, String dateTo,
                                                          String partOfSubject, int orderBy, String orderByType) {
        Query query = this.buildQuery(responsibleId, ComplaintStatus.INTRAPROCESS.name(),
                category, page, size, partOfEmail, dateFrom, dateTo, partOfSubject, orderBy, orderByType);
        Map<String, Object> response = new HashMap<>();
        List<Complaint> complaints = this.complaintDao
                .executeForList(query.getQuery(), query.getPreparedStatementParams().toArray());
        LOG.debug("Fetched complaints: {}", complaints);
        response.put("complaints", complaints);
        response.put("complaintsCount", this.complaintDao.executeForInt(query.getCountQuery(), query.getCountParams().toArray()));
        return response;
    }


    private Query buildQuery(long responsibleId, String status, String category, int page, int size,
                             String partOfEmail, String fromStr, String toStr,
                             String partOfSubject, int orderBy, String orderByType) {
        java.sql.Date from = null, to = null;
        if (!fromStr.equals("null")) {
            from = java.sql.Date.valueOf(fromStr);
        }
        if (!toStr.equals("null")) {
            to = java.sql.Date.valueOf(toStr);
        }
        if (from != null && to != null && from.getTime() > to.getTime()) {
            throw new ConflictException("Date from must be less then to");
        }
        Query.Builder builder = new Query.Builder("complaint " +
                "inner join dbuser on complaint.user_id = dbuser.id");
        builder.where().addLikeCondition("subject", partOfSubject);
        if (!category.equals("-")) builder.and().addCondition("type = ?", category);
        if (!status.equals("-")) builder.and().addCondition("complaint.status = ?", status);
        if (from != null && to != null) builder.and().addBetweenCondition("date", from, to);
        if (from != null) builder.and().addCondition("date >= ?", from);
        if (to != null) builder.and().addCondition("date <= ?", to);
        if (responsibleId > 0) builder.and().addCondition("responsible_pmg = ?", responsibleId);
        if (partOfEmail.length() > 0) builder.and().addLikeCondition("email", partOfEmail);
        String orderByField = buildOrderBy(orderBy);
        if (orderByField.length() > 0) {
            builder.orderBy(orderByField);
            builder.orderByType(orderByType);
        }
        builder.addPaging(page, size);
        return builder.build();
    }

    private String buildOrderBy(int orderBy) {
        switch (orderBy) {
            case 0://by email
                return "email";
            case 1://by date
                return "date";
            case 2://by category
                return "type";
            case 3://by subject
                return "subject";
            default:
                return "";
        }
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
    public Map<String, Object> getAllComplaintsSearch(int page, int size, String email, String status, String category) {
        Query.Builder queryBuilder = new Query.Builder("complaint inner join dbuser on complaint.user_id = dbuser.id");
        queryBuilder.where();
        queryBuilder.addLikeCondition("email", email);
        if (!status.equals("-")) {
            queryBuilder.and().addCondition("complaint.status=?", status);
        }
        if (!category.equals("-")) {
            queryBuilder.and().addCondition("complaint.type=?", category);
        }
        queryBuilder.addPaging(page, size);

        Map<String, Object> response = new HashMap<>();
        Query query = queryBuilder.build();
        response.put("complaints", complaintDao.executeForList(query.getQuery(),query.getPreparedStatementParams().toArray()));
        response.put("entitiesSelected", complaintDao.executeForInt(query.getCountQuery(),query.getCountParams().toArray()));
        return response;
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

        if (statisticsList.size() == 0) {
            throw new EmptyResultSetException("There were no complaints orders in this region during " +
                    "this period");
        }

        return this.statisticsService
                .prepareStatisticsDataSet("Complaints", statisticsList,
                        startDate, endDate);
    }
}
