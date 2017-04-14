package com.phonecompany.service;

import com.phonecompany.dao.interfaces.CrudDao;
import com.phonecompany.dao.interfaces.UserDao;
import com.phonecompany.model.User;
import com.phonecompany.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends CrudServiceImpl<User> implements UserService {

    @Autowired
    public UserServiceImpl(CrudDao<User> dao) {
        super(dao);
    }

    @Override
    public User findByUsername(String userName) {
        return ((UserDao)dao).findByUsername(userName);
    }
}
