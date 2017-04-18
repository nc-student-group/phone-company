package com.phonecompany.dao;

import com.phonecompany.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;


@Ignore
public class UserDaoImplTest extends AbstractTest{

    @Autowired
    private UserDaoImpl userDao;

    @Autowired
    private AddressDaoImpl addressDao;

    @Autowired
    private RoleDaoImpl roleDao;

    @Before
    public void setUp(){
        super.setUp();
        userDao.setAutoCommit(false);
    }

    @Test
    public void save() throws Exception{
        User user = new User();
        user.setEmail("w@w");
        user.setPassword("12345");
        userDao.save(user);
        Assert.assertFalse("User id = null", user.getId() == null);
    }

    @Test
    public void findByUsername() throws Exception {
        List<User> userList = userDao.getAll();
        User user = userDao.findByEmail(userList.get(0).getEmail());
        Assert.assertFalse("User id = null", user.getId() == null);
    }

    @Test
    public void getAll() throws Exception {
        List<User> userList = userDao.getAll();
        User user = userDao.findByEmail(userList.get(0).getEmail());
        Assert.assertFalse("User list = null", userList == null);
        Assert.assertFalse("User = null", user == null);
    }

    @Test
    public void update() throws Exception{
        // update all
        User user = new User();
        Long testId = userDao.getAll().get(0).getId();
        user.setId(testId);
        user.setEmail("w@w");
        user.setPassword("12345");
        user.setFirstName("");
        user.setSecondName("");
        user.setLastName("");
        user.setAddress(addressDao.getAll().get(0));
        user.setPhone("");
        user.setRole(roleDao.getAll().get(0));
        User newUser = userDao.update(user);
        Assert.assertNotNull("User id = null", user.getId());
        Assert.assertEquals("User field \"Email\" is not change",
                user.getEmail(), newUser.getEmail());
        Assert.assertEquals("User field \"Password\" is not change",
                user.getPassword(), newUser.getPassword());
    }

    @Test
    public void getById() throws Exception {
        Long testId = userDao.getAll().get(0).getId();
        User user = userDao.getById(testId);
        assertNotNull("User by id = null", user);
    }

    @Test
    public void delete() throws Exception {
        User user = new User();
        user.setEmail("User1");
        user.setPassword("12345");
        userDao.save(user);

        Long testId = user.getId();
        userDao.delete(testId);
        user = userDao.getById(testId);
        Assert.assertNull("User is not deleted", user);
    }

    @Test
    public void getQuery() throws Exception {
        Assert.assertNotNull("Query \"getById\" is not found", userDao.getQuery("getById"));
        Assert.assertNotNull("Query \"save\" is not found", userDao.getQuery("save"));
        Assert.assertNotNull("Query \"update\" is not found", userDao.getQuery("update"));
        Assert.assertNotNull("Query \"delete\" is not found", userDao.getQuery("delete"));
        Assert.assertNotNull("Query \"getAll\" is not found", userDao.getQuery("getAll"));
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
        String query = userDao.getQuery("getAll");
        ResultSet rs = userDao.executeSelect(query, params);
        if(!rs.isClosed()) {
            User user = userDao.init(rs);
            assertNotNull("User is not initialized", user);
        }
    }

    @Test
    public void executeSelect() throws Exception {
        Map<Integer, Object> params = new HashMap<>();
        String query = userDao.getQuery("getAll");
        ResultSet rs = userDao.executeSelect(query, params);
        Assert.assertFalse("Select is not execute (this ResultSet is closed)", rs.isClosed());
    }


}