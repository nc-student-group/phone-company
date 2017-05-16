package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.OrderDao;
import com.phonecompany.model.Order;
import com.phonecompany.service.interfaces.ServiceService;
import com.phonecompany.service.interfaces.TariffService;
import com.phonecompany.service.xssfHelper.SheetDataSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class OrderDaoTest {

    @Autowired
    private TariffService tariffService;

    @Test
    public void shouldPrepareStatisticsReportDataSet() {
        //given
        long regionId = 1L;
        LocalDate startDate = LocalDate.of(2017, Month.MAY, 1);
        LocalDate endDate = LocalDate.of(2017, Month.MAY, 5);

        //when
        SheetDataSet<LocalDate, Long> statisticsDataSet = tariffService
                .prepareStatisticsDataSet(regionId, startDate, endDate);

        //then
        System.out.println(statisticsDataSet);
    }

}
