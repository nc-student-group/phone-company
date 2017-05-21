package com.phonecompany.dao.interfaces;

import com.phonecompany.model.User;
import com.phonecompany.util.Query;

import java.util.List;

public interface UserDao extends JdbcOperations<User>, AbstractUserDao<User> {
    List<User> getAllUsersSearch(Query query);
}
