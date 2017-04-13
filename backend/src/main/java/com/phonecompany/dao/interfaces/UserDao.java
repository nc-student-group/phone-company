package com.phonecompany.dao.interfaces;

import com.phonecompany.model.User;

public interface UserDao {
    User findByUsername(String userName);
}
