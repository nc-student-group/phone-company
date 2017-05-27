package com.phonecompany.dao;

import com.phonecompany.TestUtil;
import com.phonecompany.dao.interfaces.CustomerDao;
import com.phonecompany.dao.interfaces.UserDao;
import com.phonecompany.model.Customer;
import com.phonecompany.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CustomerDaoTest {

    @Autowired
    private CustomerDao customerDao;

    @Test
    public void shouldSaveCustomer() {
        //given
        Customer sampleCustomer = TestUtil.getSampleCustomer();

        //when
        Customer peristedCustomer = customerDao.save(sampleCustomer);

        //then

    }
}
