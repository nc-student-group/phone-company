package com.phonecompany.dao;

import com.phonecompany.model.VerificationToken;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Oksanka on 15.04.2017.
 */
public class VerificationTokenDaoImplTest extends AbstractTest {
    @Autowired
    private VerificationTokenDaoImpl vtDao;

    @Autowired
    private UserDaoImpl userDao;

    @Before
    public void setUp(){
        super.setUp();
        vtDao.setAutoCommit(false);
    }

    @Test
    public void save() throws Exception{
        VerificationToken vt = new VerificationToken();
        vt.setUser(userDao.getAll().get(8)); // userId in table verification-token id not duplicate
        vt.setToken("token1");
        vt.setExpireDate(new Date(0).toLocalDate());
        VerificationToken newVerificationToken = vtDao.save(vt);
        assertNotNull("VerificationToken id = null", newVerificationToken.getId());
        assertEquals("VerificationToken field \"User\" is not change",
                vt.getUser(), newVerificationToken.getUser());
        assertEquals("VerificationToken field \"Token\" is not change",
                vt.getToken(), newVerificationToken.getToken());
        assertEquals("VerificationToken field \"ExpireDate\" is not change",
                vt.getExpireDate(), newVerificationToken.getExpireDate());
    }

    @Test
    public void update() throws Exception{
        VerificationToken vt = new VerificationToken();
        Long testId = vtDao.getAll().get(8).getId();
        vt.setId(testId);
        vt.setUser(userDao.getAll().get(0));
        vt.setToken("token1");
        vt.setExpireDate(new Date(0).toLocalDate());
        VerificationToken newVerificationToken = vtDao.update(vt);
        assertNotNull("VerificationToken id = null", vt.getId());
        assertEquals("VerificationToken field \"User\" is not change",
                vt.getUser(), newVerificationToken.getUser());
        assertEquals("VerificationToken field \"Token\" is not change",
                vt.getToken(), newVerificationToken.getToken());
        assertEquals("VerificationToken field \"ExpireDate\" is not change",
                vt.getExpireDate(), newVerificationToken.getExpireDate());
    }

    @Test
    public void getById() throws Exception {
        Long testId = vtDao.getAll().get(0).getId();
        VerificationToken vt = vtDao.getById(testId);
        assertNotNull("VerificationToken by id = null", vt);
    }

    @Test
    public void delete() throws Exception {
        VerificationToken vt = new VerificationToken();
        vt.setUser(userDao.getAll().get(0));
        vt.setToken("token1");
        vt.setExpireDate(new Date(0).toLocalDate());
        vt = vtDao.save(vt);

        Long testId = vt.getId();
        vtDao.delete(testId);
        vt = vtDao.getById(testId);
        Assert.assertNull("VerificationToken is not deleted", vt);
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
        assertNotNull("Query \"delete\" is not found", vtDao.getQuery("delete"));
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