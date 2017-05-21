package com.phonecompany.service;

import com.phonecompany.dao.interfaces.OrderDao;
import com.phonecompany.model.CustomerTariff;
import com.phonecompany.model.Order;
import com.phonecompany.model.Tariff;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class NullTest {

    private static final Logger LOG = LoggerFactory.getLogger(NullTest.class);

    @Autowired
    private OrderDao orderDao;

    @Test
    public void shouldReturnNull() {
        //given
        Order order = orderDao.getById(76L);

        LOG.debug("order.getCustomerTariff(): {}", order.getCustomerTariff());
        LOG.debug("order.getCustomerTariff() != null: {}", order.getCustomerTariff() != null);
    }
}
