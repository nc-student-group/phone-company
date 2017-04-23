package com.phonecompany.dao;

import com.phonecompany.model.VerificationToken;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertNotNull;

public class VerificationTokenDaoImplTest extends AbstractTest {

    @Autowired
    private VerificationTokenDaoImpl vtDao;

    @Autowired
    private UserDaoImpl userDao;

    @Before
    public void setUp(){
        vtDao.setAutoCommit(false);
    }

    @Test
    public void save() throws Exception{
    }

    @Test
    public void update() throws Exception{
    }

    @Test
    public void getById() throws Exception {
    }

    @Test
    public void delete() throws Exception {

    }

    @Test
    public void getAll() throws Exception {
        List<VerificationToken> vts = vtDao.getAll();

        assertNotNull("VerificationToken table is empty", vts);
    }

    @Test
    public void getQuery() throws Exception {
        assertNotNull("Query \"getById\" is not found", vtDao.getQuery("getById"));
        assertNotNull("Query \"save\" is not found", vtDao.getQuery("save"));
        assertNotNull("Query \"update\" is not found", vtDao.getQuery("update"));
        assertNotNull("Query \"getAll\" is not found", vtDao.getQuery("getAll"));
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
        String query = vtDao.getQuery("getAll");
        ResultSet rs = vtDao.executeSelect(query, params);
        if(!rs.isClosed()) {
            VerificationToken vt = vtDao.init(rs);
            assertNotNull("VerificationToken is not initialized", vt);
        }*/
    }

    @Test
    public void executeSelect() throws Exception {
    /*    VerificationToken vt = new VerificationToken();
        Map<Integer, Object> params = new HashMap<>();
        String query = vtDao.getQuery("getAll");
        ResultSet rs = vtDao.executeSelect(query, params);
        Assert.assertFalse("Select is not execute (this ResultSet is closed)", rs.isClosed());
    */}
}