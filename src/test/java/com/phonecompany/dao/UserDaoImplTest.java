package com.phonecompany.dao;

import com.phonecompany.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Ignore
public class UserDaoImplTest extends AbstractTest{

    @Autowired
    private UserDaoImpl userDao;

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
        User user = userDao.findByUsername(userList.get(0).getEmail());
        Assert.assertFalse("User id = null", user.getId() == null);
    }

    @Test
    public void getAll() throws Exception {
        List<User> userList = userDao.getAll();
        User user = userDao.findByUsername(userList.get(0).getEmail());
        Assert.assertFalse("User list = null", userList == null);
        Assert.assertFalse("User = null", user == null);
    }


}