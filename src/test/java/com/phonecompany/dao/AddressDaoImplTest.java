package com.phonecompany.dao;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Ignore
public class AddressDaoImplTest extends AbstractTest {

    @Autowired
    private AddressDaoImpl addressDao;

    @Before
    public void setUp() {
        addressDao.setAutoCommit(false);
    }

    @Test
    public void save() throws Exception {
    }

    @Test
    public void update() throws Exception {
    }

    @Test
    public void getById() throws Exception {
    }

    @Test
    public void delete() throws Exception {
    }

    @Test
    public void getAll() throws Exception {
    }

    @Test
    public void getQuery() throws Exception {

    }

    @Test
    public void populateSaveStatement() throws Exception {

    }

    @Test
    public void populateUpdateStatement() throws Exception {

    }

    @Test
    public void init() throws Exception {
        /*Map<Integer, Object> params = new HashMap<>();
        String query = addressDao.getQuery("getAll");
        ResultSet rs = addressDao.executeSelect(query, params);
        if(!rs.isClosed()) {
            Address address = addressDao.init(rs);
            Assert.assertNotNull("Address is not initialized", address);
        }*/
    }

    @Test
    public void executeSelect() throws Exception {
       /* Address address = new Address();
        Map<Integer, Object> params = new HashMap<>();
        String query = addressDao.getQuery("getAll");
        ResultSet rs = addressDao.executeSelect(query, params);
        Assert.assertFalse("Select is not execute (this ResultSet is closed)", rs.isClosed());
    */
    }
}
