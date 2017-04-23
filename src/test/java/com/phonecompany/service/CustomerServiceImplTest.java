package com.phonecompany.service;

import com.phonecompany.dao.interfaces.CustomerDao;
import com.phonecompany.model.Customer;
import com.phonecompany.model.enums.Status;
import org.junit.Before;
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
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnCustomerById() {
        long id = 1L;
        Customer customer = createCustomer(id);

        when(customerDao.getById(id)).thenReturn(customer);

        customer = customerService.getById(id);

        assertThat(id, equalTo(customer.getId()));
    }

    @Test
    public void shouldUpdateCustomer() {
        long id = 1L;
        Customer customer = createCustomer(id);

        customerService.update(customer);

        verify(customerDao, atLeastOnce()).update(customer);
    }

    @Test
    public void shouldSaveCustomer() {
        long id = 1L;
        Customer customer = createCustomer(id);

        customerService.save(customer);

        verify(customerDao, atLeastOnce()).save(customer);
    }

    @Test
    public void shouldGetAllCustomers() {
        long id1 = 1L;
        Customer customer1 = createCustomer(id1);

        long id2 = 2L;
        Customer customer2 = createCustomer(id2);

        when(customerDao.getAll()).thenReturn(Arrays.asList(customer1, customer2));

        customerService.getAll();

        assertThat(id1, equalTo(customer1.getId()));

        assertThat(id2, equalTo(customer2.getId()));
    }

    @Test
    public void shouldGetStatus() throws Exception {
    }

    @Test
    public void shouldConfirmRegistration() throws Exception {
    }

    @Test
    public void shouldActivateUserByToken() throws Exception {
        long id = 1L;
        String token = "token";
        Customer customer = createCustomer(id);

        when(customerDao.getByVerificationToken(token)).thenReturn(customer);

        customerService.activateUserByToken(token);

        verify(customerDao, atLeastOnce()).update(customer);

        assertThat(Status.ACTIVATED, equalTo(customer.getStatus()));

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