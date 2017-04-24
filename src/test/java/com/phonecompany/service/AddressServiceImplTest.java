package com.phonecompany.service;

import com.phonecompany.dao.interfaces.AddressDao;
import com.phonecompany.model.Address;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
public class AddressServiceImplTest {

    @Mock
    private AddressDao addressDao;

    @InjectMocks
    private AddressServiceImpl addressService;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnAddressById() {
        String country = "Ukraine";
        String region = "Kiev";
        long id = 1L;
        Address address = createAddress(id, country, region);

        when(addressDao.getById(id)).thenReturn(address);

        address = addressService.getById(id);

        assertThat(id, equalTo(address.getId()));
    }

    @Test
    public void shouldUpdateAddress() {
        String country = "Ukraine";
        String region = "Kiev";
        long id = 1L;
        Address address = createAddress(id, country, region);

        addressService.update(address);

        verify(addressDao, atLeastOnce()).update(address);
    }

    @Test
    public void shouldSaveAddress() {
        String country = "Ukraine";
        String region = "Kiev";
        long id = 1L;
        Address address = createAddress(id, country, region);

        addressService.save(address);

        verify(addressDao, atLeastOnce()).save(address);
    }

    @Test
    public void shouldGetAllAddresses() {
        String country1 = "Ukraine";
        String region1 = "Kiev";
        long id1 = 1L;
        Address address1 = createAddress(id1, country1, region1);

        String country2 = "Spain";
        String region2 = "Madrid";
        long id2 = 2L;
        Address address2 = createAddress(id2, country2, region2);

        when(addressDao.getAll()).thenReturn(Arrays.asList(address1, address2));

        addressService.getAll();

        assertThat(id1, equalTo(address1.getId()));

        assertThat(id2, equalTo(address2.getId()));
    }


    private Address createAddress(Long id, String country, String region) {
        Address address = new Address();
        address.setId(id);
        return address;
    }

}