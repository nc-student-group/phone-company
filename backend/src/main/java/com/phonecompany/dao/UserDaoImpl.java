package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.UserDao;
import com.phonecompany.exception.EntityInitializationException;
import com.phonecompany.model.User;
import com.phonecompany.util.QueryLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Repository
public class UserDaoImpl extends CrudDaoImpl<User>
        implements UserDao {

    private QueryLoader queryLoader;

    @Autowired
    public UserDaoImpl(QueryLoader queryLoader) {
        this.queryLoader = queryLoader;
    }

    @Override
    public User findByUsername(String userName) {
        return null;
    }

    @Override
    public String getQuery(String type) {
        return queryLoader.getQuery("query.user." + type);
    }

    @Override
    public Map<Integer, Object> getParams(User user) {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, user.getEmail());
        params.put(2, user.getPassword());
        return params;
    }

    @Override
    public void setId(User user, long id) {
        user.setId(id);
    }

    @Override
    public User init(ResultSet rs) {
        User user = new User();
        try {
            user.setId(rs.getLong("id"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}
