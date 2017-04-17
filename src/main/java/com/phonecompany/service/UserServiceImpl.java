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
    private ShaPasswordEncoder shaPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao, ShaPasswordEncoder shaPasswordEncoder) {
        super(userDao);
        this.userDao = userDao;
        this.shaPasswordEncoder = shaPasswordEncoder;
    }

    @Override
    public User findByUsername(String userName) {
        return userDao.findByUsername(userName);
    }

    @Override
    public User resetPassword(User user) {
        user.setPassword(generatePassword());
        return update(user);
    }

    private String generatePassword() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(50, random).toString(32);
    }

    public User save(User user){
        user.setPassword(shaPasswordEncoder.encodePassword(user.getPassword(), null));
        return super.save(user);
    }

    public User update(User user){
        user.setPassword(shaPasswordEncoder.encodePassword(user.getPassword(), null));
        return super.update(user);
    }

}