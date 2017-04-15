package com.phonecompany.interfaces;

import com.phonecompany.model.User;

public interface UserDao extends CrudDao<User> {
    User findByUsername(String userName);
}
