package com.phonecompany.dao.interfaces;

import com.phonecompany.model.User;

import java.util.List;

public interface UserDao extends CrudDao<User>, AbstractUserDao<User> {
    public List<User> getAllUsersPaging(int page, int size, int role, String status);
    public int getCountUsers(int role,String status);
}
