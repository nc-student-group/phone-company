package com.phonecompany.dao;

import com.phonecompany.model.Address;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Oksanka on 15.04.2017.
 */
public class AddressDaoImplTest extends AbstractTest {
    @Autowired
    private AddressDaoImpl addressDao;

    @Before
    public void setUp(){
        super.setUp();
        addressDao.setAutoCommit(false);
    }

    @Test
    public void save() throws Exception{
        Address address = new Address();
        address.setCountry("Country1");
        address.setRegion("Region1");
        address.setSettlement("Settlement1");
        address.setStreet("St1");
        address.setHouseNumber("1");
        addressDao.save(address);
        Assert.assertNotNull("Address id = null", address.getId());
    }

    @Test
    public void update() throws Exception{
        Address address = new Address();
        Long testId = addressDao.getAll().get(0).getId();
        address.setId(testId);
        address.setCountry("Country2");
        Address newAddress = addressDao.update(address);
        Assert.assertNotNull("Address id = null", address.getId());
        Assert.assertEquals("Address field is not change",
                            address.getCountry(), newAddress.getCountry());
    }

    @Test
    public void getById() throws Exception {
        Long testId = addressDao.getAll().get(0).getId();
        Address address = addressDao.getById(testId);
        Assert.assertNotNull("Address by id = null", address);
    }

    @Test
    public void delete() throws Exception {
        Address address = new Address();
        address.setCountry("Country1");
        address.setRegion("Region1");
        address.setSettlement("Settlement1");
        address.setStreet("St1");
        address.setHouseNumber("1");
        addressDao.save(address);

        Long testId = address.getId();
        address = addressDao.getById(testId);
        addressDao.delete(testId);
        address = addressDao.getById(testId);
        Assert.assertNull("Address is not deleted", address);
    }

    @Test
    public void getAll() throws Exception {
        List<Address> addresses = addressDao.getAll();

        Assert.assertNotNull("Address table is empty", addresses);
    }

    @Test
    public void getQuery() throws Exception {
        String queryGetById =  addressDao.getQuery("getById");
        String querySave =  addressDao.getQuery("save");
        String queryUpdate =  addressDao.getQuery("update");
        String queryDelete =  addressDao.getQuery("delete");
        String queryGetAll =  addressDao.getQuery("getAll");
        Assert.assertNotNull("Query \"getById\" is not found", queryGetById);
        Assert.assertNotNull("Query \"save\" is not found", querySave);
        Assert.assertNotNull("Query \"update\" is not found", queryUpdate);
        Assert.assertNotNull("Query \"delete\" is not found", queryDelete);
        Assert.assertNotNull("Query \"getAll\" is not found", queryGetAll);
    }

    @Test
    public void populateSaveStatement() throws Exception {

    }

    @Test
    public void populateUpdateStatement() throws Exception {

    }

    @Test
    public void init() throws Exception {
        Map<Integer, Object> params = new HashMap<>();
        String query = addressDao.getQuery("getAll");
        ResultSet rs = addressDao.executeSelect(query, params);
        if(!rs.isClosed()) {
            Address address = addressDao.init(rs);
            Assert.assertNotNull("Address is not initialized", address);
        }
    }

    @Test
    public void executeSelect() throws Exception {
        Address address = new Address();
        Map<Integer, Object> params = new HashMap<>();
        String query = addressDao.getQuery("getAll");
        ResultSet rs = addressDao.executeSelect(query, params);
        Assert.assertFalse("Select is not execute (this ResultSet is closed)", rs.isClosed());
    }
}
