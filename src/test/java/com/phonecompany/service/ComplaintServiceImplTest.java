package com.phonecompany.service;

import com.phonecompany.dao.interfaces.ComplaintDao;
import com.phonecompany.model.Complaint;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Ignore
public class ComplaintServiceImplTest {

    @Mock
    private ComplaintDao complaintDao;

    @InjectMocks
    private ComplaintServiceImpl complaintService;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnComplaintById() {
        long id = 1L;
        Complaint complaint = createComplaint(id);

        when(complaintDao.getById(id)).thenReturn(complaint);

        complaint = complaintService.getById(id);

        assertThat(id, equalTo(complaint.getId()));
    }

    @Test
    public void shouldUpdateComplaint() {
        long id = 1L;
        Complaint complaint = createComplaint(id);

        complaintService.update(complaint);

        verify(complaintDao, atLeastOnce()).update(complaint);
    }

    @Test
    public void shouldSaveComplaint() {
        long id = 1L;
        Complaint complaint = createComplaint(id);

        complaintService.save(complaint);

        verify(complaintDao, atLeastOnce()).save(complaint);
    }

    @Test
    public void shouldGetAllComplaints() {
        long id1 = 1L;
        Complaint complaint1 = createComplaint(id1);

        long id2 = 2L;
        Complaint complaint2 = createComplaint(id2);

        when(complaintDao.getAll()).thenReturn(Arrays.asList(complaint1, complaint2));

        complaintService.getAll();

        assertThat(id1, equalTo(complaint1.getId()));

        assertThat(id2, equalTo(complaint2.getId()));
    }


    private Complaint createComplaint(Long id) {
        Complaint complaint = new Complaint();
        complaint.setId(id);
        complaint.setText("This is a complaint!");
        return complaint;
    }
}