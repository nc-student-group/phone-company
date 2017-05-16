package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.ComplaintDao;
import com.phonecompany.model.Complaint;
import com.phonecompany.model.enums.ComplaintCategory;
import com.phonecompany.model.enums.ComplaintStatus;
import com.phonecompany.service.xssfHelper.RowDataSet;
import com.phonecompany.service.xssfHelper.SheetDataSet;
import com.phonecompany.service.xssfHelper.TableDataSet;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

//@Ignore
public class AddressDaoImplTest extends AbstractTest {

    @Autowired
    private AddressDaoImpl addressDao;

    @Before
    public void setUp() {
//        addressDao.setAutoCommit(false);
    }
    @Autowired
    private ComplaintDao complaintDao;

    @Test
    public void save() throws Exception {
        List<Complaint> complaints = complaintDao.getComplaintsByRegionId(10L);
        complaints = filterComplaintsByDate(complaints, LocalDate.of(2017,5,1), LocalDate.now());

        Map<ComplaintStatus, List<Complaint>> statusToComplaintsMap = this
                .getStatusToComplaintsMap(complaints);

        List<LocalDate> timeLine = this.generateTimeLine(complaints);

        SheetDataSet sheet =  this.prepareExcelSheetDataSet("Complaints", statusToComplaintsMap, timeLine);

//        for (TableDataSet s: sheet.getTableDataSets()) {
//            System.out.println(s);
//        }
        System.out.println("-------TimeLine-----------\n");
        for (LocalDate t: timeLine) {
            System.out.println(t);
        }
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
            row.addKeyValuePair(complaintNumberByDate, date);
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

    @Test
    public void update() throws Exception {
    }

    @Test
    public void getById() throws Exception {
    }

    @Test
    public void delete() throws Exception {
    }

    @Test
    public void getAll() throws Exception {
    }

    @Test
    public void getQuery() throws Exception {

    }

    @Test
    public void populateSaveStatement() throws Exception {

    }

    @Test
    public void populateUpdateStatement() throws Exception {

    }

    @Test
    public void init() throws Exception {
        /*Map<Integer, Object> params = new HashMap<>();
        String query = addressDao.getQuery("getAll");
        ResultSet rs = addressDao.executeSelect(query, params);
        if(!rs.isClosed()) {
            Address address = addressDao.init(rs);
            Assert.assertNotNull("Address is not initialized", address);
        }*/
    }

    @Test
    public void executeSelect() throws Exception {
       /* Address address = new Address();
        Map<Integer, Object> params = new HashMap<>();
        String query = addressDao.getQuery("getAll");
        ResultSet rs = addressDao.executeSelect(query, params);
        Assert.assertFalse("Select is not execute (this ResultSet is closed)", rs.isClosed());
    */
    }
}
