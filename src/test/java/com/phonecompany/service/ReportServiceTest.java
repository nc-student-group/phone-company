package com.phonecompany.service;

import com.phonecompany.service.interfaces.TariffService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.time.Month;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ReportServiceTest {

    @Autowired
    private TariffService tariffService;

    @Test
    public void shouldCreateXls() {
        //given
        long regionId = 1;
        LocalDate startDate = LocalDate.of(2017, Month.MAY, 1);
        LocalDate endDate = LocalDate.of(2017, Month.MAY, 5);

        //then
        tariffService.generateTariffReport(regionId, startDate, endDate);
    }
}
