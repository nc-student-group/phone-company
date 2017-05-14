package com.phonecompany.dao.interfaces;

import com.phonecompany.model.User;
import com.phonecompany.util.Query;

import java.util.List;
import java.util.Map;

public interface UserDao extends CrudDao<User>, AbstractUserDao<User>,
        AbstractPageableDao<User> {
    List<User> getAllUsersSearch(Query query);
}
