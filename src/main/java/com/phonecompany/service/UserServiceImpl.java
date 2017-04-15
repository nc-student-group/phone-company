package com.phonecompany.service;

import com.phonecompany.interfaces.CrudDao;
import com.phonecompany.interfaces.UserDao;
import com.phonecompany.model.User;
import com.phonecompany.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;

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

    @Override
    public User resetPassword(User user) {
        user.setPassword(generatePassword());

        //TODO: sending password by email

        return update(user);
    }

    private String generatePassword() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(50, random).toString(32);
    }
}
