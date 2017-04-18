package com.phonecompany.dao;

import com.phonecompany.model.Role;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Oksanka on 15.04.2017.
 */
public class RoleDaoImplTest extends AbstractTest {
    @Autowired
    private RoleDaoImpl roleDao;

    @Before
    public void setUp(){
        super.setUp();
        roleDao.setAutoCommit(false);
    }

    @Test
    public void save() throws Exception{
        Role role = new Role();
        role.setName("role1");
        roleDao.save(role);
        assertNotNull("Role id = null", role.getId());
    }

    @Test
    public void update() throws Exception{
        Role role = new Role();
        Long testId = roleDao.getAll().get(0).getId();
        role.setId(testId);
        role.setName("Role1");
        Role newRole = roleDao.update(role);
        Assert.assertNotNull("Role id = null", role.getId());
        Assert.assertEquals("Role field is not change",
                role.getName(), newRole.getName());
    }

    @Test
    public void getById() throws Exception {
        Long testId = roleDao.getAll().get(0).getId();
        Role role = roleDao.getById(testId);
        assertNotNull("Role by id = null", role);
    }

    @Test
    public void delete() throws Exception {
        Role role = new Role();
        role.setName("Role1");
        roleDao.save(role);

        Long testId = role.getId();
        roleDao.delete(testId);
        role = roleDao.getById(testId);
        Assert.assertNull("Role is not deleted", role);
    }

    @Test
    public void getAll() throws Exception {
        List<Role> roles = roleDao.getAll();

        assertNotNull("Role table is empty", roles);
    }

    @Test
    public void getQuery() throws Exception {
        String queryGetById =  roleDao.getQuery("getById");
        String querySave =  roleDao.getQuery("save");
        String queryUpdate =  roleDao.getQuery("update");
        String queryDelete =  roleDao.getQuery("delete");
        String queryGetAll =  roleDao.getQuery("getAll");
        assertNotNull("Query \"getById\" is not found", queryGetById);
        assertNotNull("Query \"save\" is not found", querySave);
        assertNotNull("Query \"update\" is not found", queryUpdate);
        assertNotNull("Query \"delete\" is not found", queryDelete);
        assertNotNull("Query \"getAll\" is not found", queryGetAll);
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
        String query = roleDao.getQuery("getAll");
        ResultSet rs = roleDao.executeSelect(query, params);
        if(!rs.isClosed()) {
            Role role = roleDao.init(rs);
            assertNotNull("Role is not initialized", role);
        }*/
    }

    @Test
    public void executeSelect() throws Exception {
       /* Role role = new Role();
        Map<Integer, Object> params = new HashMap<>();
        String query = roleDao.getQuery("getAll");
        ResultSet rs = roleDao.executeSelect(query, params);
        Assert.assertFalse("Select is not execute (this ResultSet is closed)", rs.isClosed());
    */}
}
