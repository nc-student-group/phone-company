package com.phonecompany.dao;

import com.phonecompany.UserDaoImpl;
import com.phonecompany.dao.AbstractTest;
import com.phonecompany.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by Yurii on 14.04.2017.
 */
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

    }


}