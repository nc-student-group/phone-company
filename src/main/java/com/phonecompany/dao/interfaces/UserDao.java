package com.phonecompany.dao.interfaces;

import com.phonecompany.model.User;

import java.util.List;
import java.util.Map;

public interface UserDao extends CrudDao<User>, AbstractUserDao<User>,
        AbstractPageableDao<User> {
    List<User> getAllUsersSearch(String email, int userRole, String status)
}
