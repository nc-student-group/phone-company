package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.UserDao;
import com.phonecompany.model.User;
import com.phonecompany.util.QueryLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDaoImpl extends CrudDaoImpl<User> implements UserDao {

    @Autowired
    private QueryLoader queryLoader;

    @Override
    public User findByUsername(String userName) {
        return null;
    }

    @Override
    public String getQuery(String type){
        return queryLoader.getQuery("query.user." + type);
    }

    @Override
    public void setParamsForSaveStatement(User user, PreparedStatement preparedStatement){
        try {
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2 ,user.getPassword());
        } catch (SQLException e) {
            e.printStackTrace();
            //TODO: exception handling
        }
    }

    @Override
    public void setId(User user, long id){
        user.setId(id);
    }

    @Override
    public User init(ResultSet rs){
        User user = new User();
        try {
            user.setId(rs.getLong("id"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
        } catch (SQLException e) {
            e.printStackTrace();
            //TODO: exception handling
        }
        return user;
    }

}
