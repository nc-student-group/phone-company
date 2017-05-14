package com.phonecompany.dao;

import com.phonecompany.TestUtil;
import com.phonecompany.dao.interfaces.ServiceDao;
import com.phonecompany.model.Service;
import com.phonecompany.util.QueryLoader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ServiceDaoImplTest {

    @Autowired
    private ServiceDao serviceDao;

    @Test
    public void shouldSaveService() {
        //given
        Service sampleService = TestUtil.getSampleService();

        //when
        serviceDao.save(sampleService);

        //then
        Service serviceFoundById = serviceDao.getById(sampleService.getId());

        assertThat(serviceFoundById.getServiceName(), is(sampleService.getServiceName()));
        assertThat(serviceFoundById.getPrice(), is(sampleService.getPrice()));
        assertThat(serviceFoundById.getProductStatus(), is(sampleService.getProductStatus()));
        assertThat(serviceFoundById.getDiscount(), is(sampleService.getDiscount()));
    }
}
