package com.phonecompany.service;

import com.phonecompany.dao.interfaces.CustomerDao;
import com.phonecompany.model.Customer;
import com.phonecompany.model.enums.Status;
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

public class CustomerServiceImplTest {

    @Mock
    private CustomerDao customerDao;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldFindCustomerByEmail() throws Exception {
        long id = 1L;
        String email = "test@mail.com";
        Customer customer = createCustomer(id);
        customer.setEmail(email);

        when(customerDao.findByEmail(email)).thenReturn(customer);

        customer = customerService.findByEmail(email);

        assertThat(id, equalTo(customer.getId()));
        assertThat(email, equalTo(customer.getEmail()));
    }

    private Customer createCustomer(Long id) {
        Customer customer = new Customer();
        String email = "test@mail.com";
        customer.setId(id);
        customer.setEmail(email);
        return customer;
    }

}