package com.phonecompany.service;

import com.phonecompany.annotations.ServiceStereotype;
import com.phonecompany.dao.interfaces.ComplaintDao;
import com.phonecompany.model.Complaint;
import com.phonecompany.model.ComplaintStatistics;
import com.phonecompany.model.User;
import com.phonecompany.model.enums.ComplaintCategory;
import com.phonecompany.model.enums.ComplaintStatus;
import com.phonecompany.model.enums.WeekOfMonth;
import com.phonecompany.service.interfaces.ComplaintService;
import com.phonecompany.service.interfaces.UserService;
import com.phonecompany.service.xssfHelper.RowDataSet;
import com.phonecompany.service.xssfHelper.SheetDataSet;
import com.phonecompany.service.xssfHelper.TableDataSet;
import com.phonecompany.util.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

@ServiceStereotype
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

    @Override
    public List<Complaint> getAllComplaintsSearch(int page, int size,String email, String status, String category) {
        Query.Builder query = new Query.Builder("complaint inner join dbuser on complaint.user_id = dbuser.id");
        query.where();
        query.addLikeCondition("email",email);
        if(!status.equals("-")){
            query.and().addCondition("complaint.status=?",status);
        }
        if(!category.equals("-")){
            query.and().addCondition("complaint.type=?",category);
        }
        query.addPaging(page,size);
        return complaintDao.getAllComplaintsSearch(query.build());
    }

    @Override
    public int getCountSearch(int page, int size, String email, String status, String category) {
        Query.Builder query = new Query.Builder("complaint inner join dbuser on complaint.user_id = dbuser.id");
        query.where();
        query.addLikeCondition("dbuser.email",email);
        if(!status.equals("-")){
            query.and().addCondition("complaint.status=?",status);
        }
        if(!category.equals("-")){
            query.and().addCondition("complaint.type=?",category);
        }
        return complaintDao.getAllComplaintsSearch(query.build()).size();
    }

    @Override
    public ComplaintStatistics getComplaintStatistics() {

        EnumMap<WeekOfMonth, Integer> numberOfCSComplaintsForTheLastMonth =
                this.complaintDao.getNumberOfComplaintsForTheLastMonthByCategory(ComplaintCategory.CUSTOMER_SERVICE);
        EnumMap<WeekOfMonth, Integer> numberOfSComplaintsForTheLastMonth =
                this.complaintDao.getNumberOfComplaintsForTheLastMonthByCategory(ComplaintCategory.SUGGESTION);
        EnumMap<WeekOfMonth, Integer> numberOfTSComplaintsForTheLastMonth =
                this.complaintDao.getNumberOfComplaintsForTheLastMonthByCategory(ComplaintCategory.TECHNICAL_SERVICE);

        return new ComplaintStatistics(numberOfCSComplaintsForTheLastMonth,
                numberOfSComplaintsForTheLastMonth,
                numberOfTSComplaintsForTheLastMonth);
    }

    @Override
    public SheetDataSet prepareComplaintReportDataSet(long regionId, LocalDate startDate, LocalDate endDate) {

        List<Complaint> complaints = this.getComplaintsByRegionIdAndTimePeriod(regionId, startDate, endDate);

        Map<ComplaintStatus, List<Complaint>> statusToComplaintsMap = this
                .getStatusToComplaintsMap(complaints);

        List<LocalDate> timeLine = this.generateTimeLine(complaints);

        return this.prepareExcelSheetDataSet("Complaints", statusToComplaintsMap, timeLine);
    }

    private List<Complaint> getComplaintsByRegionIdAndTimePeriod(long regionId,
                                                              LocalDate startDate,
                                                              LocalDate endDate) {
        return this.filterComplaintsByDate(
                this.complaintDao.getComplaintsByRegionId(regionId), startDate, endDate);
    }

    private List<Complaint> filterComplaintsByDate(List<Complaint> complaintList,
                                                   LocalDate startDate, LocalDate endDate) {
        return complaintList.stream()
                .filter(c -> c.getDate().isAfter(startDate) || c.getDate().isEqual(startDate))
                .filter(c -> c.getDate().isBefore(endDate) || c.getDate().isEqual(endDate))
                .collect(Collectors.toList());
    }

    private SheetDataSet prepareExcelSheetDataSet(String sheetName,
                                                 Map<ComplaintStatus, List<Complaint>> statusToComplaintsMap,
                                                 List<LocalDate> timeLine) {
        SheetDataSet sheet = new SheetDataSet(sheetName);
        List<ComplaintCategory> complaintCategories = asList(ComplaintCategory.CUSTOMER_SERVICE,
                ComplaintCategory.SUGGESTION,
                ComplaintCategory.TECHNICAL_SERVICE);
        for (ComplaintCategory complaintCategory : complaintCategories) {
            this.prepareExcelTableDataSet(sheet, complaintCategory, statusToComplaintsMap, timeLine);
        }
        return sheet;
    }

    private void prepareExcelTableDataSet(SheetDataSet sheet,
                                          ComplaintCategory complaintCategory,
                                          Map<ComplaintStatus, List<Complaint>> statusToComplaintsMap,
                                          List<LocalDate> timeLine) {
        TableDataSet table = sheet.createTable(complaintCategory.toString());
        for (ComplaintStatus complaintStatus : statusToComplaintsMap.keySet()) {
            this.prepareExcelRowDataSet(table, complaintStatus, complaintCategory,
                    statusToComplaintsMap, timeLine);
        }
    }

    private void prepareExcelRowDataSet(TableDataSet table,
                                        ComplaintStatus complaintStatus,
                                        ComplaintCategory complaintCategory,
                                        Map<ComplaintStatus, List<Complaint>> statusToComplaintsMap,
                                        List<LocalDate> timeLine) {
        RowDataSet row = table.createRow(complaintStatus.name());
        List<Complaint> complaints = statusToComplaintsMap.get(complaintStatus);
        List<Complaint> complaintsByCategory = this.filterComplaintsByCategory(complaints, complaintCategory);
        for (LocalDate date : timeLine) {
            long complaintNumberByDate = this.getComplaintsNumberByDate(complaintsByCategory, date);
            //TODO: remove unchecked compile warning
            row.addKeyValuePair(date, complaintNumberByDate);
        }
    }

    private long getComplaintsNumberByDate(List<Complaint> complaintListList,
                                          LocalDate date) {
        return complaintListList.stream()
                .filter(c -> c.getDate().equals(date))
                .count();
    }

    private List<Complaint> filterComplaintsByCategory(List<Complaint> complaints,
                                                      ComplaintCategory type) {
        return complaints.stream()
                .filter(c -> c.getType().equals(type))
                .collect(Collectors.toList());
    }

    private List<LocalDate> generateTimeLine(List<Complaint> complaints) {
        return complaints.stream()
                .map(Complaint::getDate)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    private Map<ComplaintStatus, List<Complaint>> getStatusToComplaintsMap(List<Complaint> complaints) {
        Map<ComplaintStatus, List<Complaint>> statusToComplaintsMap = new HashMap<>();
        List<ComplaintStatus> statuses = this.getAllStatus();
        for (ComplaintStatus status : statuses) {
            List<Complaint> complaintsByStatus = this.filterComplaintsByStatus(complaints, status);
            this.putComplaintsInMap(statusToComplaintsMap, status, complaintsByStatus);
        }
        return statusToComplaintsMap;
    }

    private List<ComplaintStatus> getAllStatus() {
        return Arrays.asList(ComplaintStatus.values());
    }

    private List<Complaint> filterComplaintsByStatus(List<Complaint> complaints,
                                                     ComplaintStatus status) {
        return complaints.stream()
                .filter(c -> c.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    private void putComplaintsInMap(Map<ComplaintStatus, List<Complaint>> map,
                                    ComplaintStatus key, List<Complaint> complaints) {
        if (map.get(key) == null) {
            List<Complaint> complaintsInMap = new ArrayList<>();
            complaintsInMap.addAll(complaints);
            map.put(key, complaintsInMap);
        } else {
            map.get(key).addAll(complaints);
        }
    }

}
