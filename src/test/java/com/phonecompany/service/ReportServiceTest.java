package com.phonecompany.service;

import com.phonecompany.dao.interfaces.OrderDao;
import com.phonecompany.service.interfaces.OrderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.time.LocalDate;
import java.time.Month;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ReportServiceTest {

    @Autowired
    private OrderDao orderDao;

    private XSSFServiceImpl xssfService;

    @Before
    public void setUp() {
        xssfService = new XSSFServiceImpl(orderDao);
    }

    @Test
    public void shouldCreateXls() {
        //given
        LocalDate startDate = LocalDate.of(2017, Month.MAY, 1);
        LocalDate endDate = LocalDate.of(2017, Month.MAY, 5);
        long regionId = 1;

        //when
        xssfService.generateReport(regionId, startDate, endDate);

        //then

    }

    @Test
    public void shouldFindFileAtFileSystem() {
        //given
        File dir = new File("./");

        File[] files = dir.listFiles((dir1, filename) -> filename.endsWith(".xlsx"));


        //when

        //then
    }
}
