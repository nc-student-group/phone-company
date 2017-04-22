package com.phonecompany.dao.interfaces;

import com.phonecompany.model.User;

public interface UserDao extends CrudDao<User>, AbstractUserDao<User> {
}
