package com.phonecompany.service;

import com.phonecompany.dao.interfaces.UserDao;
import com.phonecompany.model.User;
import com.phonecompany.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;

@Service
public class UserServiceImpl extends CrudServiceImpl<User>
        implements UserService {

    private UserDao userDao;
    private ShaPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao,
                           ShaPasswordEncoder shaPasswordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = shaPasswordEncoder;
    }

    public UserServiceImpl() {
    }

    @Override
    public User findByUsername(String userName) {
        return ((UserDao)dao).findByUsername(userName);
    }

    @Override
    public User save(User user) {
        user.setPassword(passwordEncoder.encodePassword(user.getPassword(), null));
        return userDao.save(user);
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
