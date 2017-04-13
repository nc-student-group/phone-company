package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.UserDao;
import com.phonecompany.exception.EntityInitializationException;
import com.phonecompany.exception.EntityPersistenceException;
import com.phonecompany.exception.PreparedStatementPopulationException;
import com.phonecompany.model.User;
import com.phonecompany.util.QueryLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    public void populateSaveStatement(PreparedStatement preparedStatement, User user){
        try {
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPassword());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }

    @Override
    public User init(ResultSet rs) {
        User user = new User();
        try {
            user.setId(rs.getLong("id"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
        } catch (SQLException e) {
            throw new EntityInitializationException(e);
        }
        return user;
    }
}
