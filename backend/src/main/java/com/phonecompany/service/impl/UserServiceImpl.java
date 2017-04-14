package com.phonecompany.service.impl;

import com.phonecompany.dao.interfaces.CrudDao;
import com.phonecompany.dao.interfaces.UserDao;
import com.phonecompany.model.User;
import com.phonecompany.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Yurii on 14.04.2017.
 */
@Service
public class UserServiceImpl extends AbstractServiceImpl<User> implements UserService{

    @Autowired
    private UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao dao) {
        super(dao);
    }
}
