package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.UserDao;
import com.phonecompany.exception.EntityInitializationException;
import com.phonecompany.exception.EntityNotFoundException;
import com.phonecompany.exception.PreparedStatementPopulationException;
import com.phonecompany.model.User;
import com.phonecompany.model.enums.Status;
import com.phonecompany.util.QueryLoader;
import com.phonecompany.util.TypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class UserDaoImpl extends AbstractUserDaoImpl<User>
        implements UserDao {

    private QueryLoader queryLoader;

    @Autowired
    public UserDaoImpl(QueryLoader queryLoader) {
        this.queryLoader = queryLoader;
    }

    @Override
    public void populateSaveStatement(PreparedStatement statement, User user) {
        try {
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            statement.setLong(3, user.getRole().getDatabaseId());
            statement.setString(4, user.getStatus().name());
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
            user.setRole(TypeMapper.getUserRoleByDatabaseId(rs.getLong("role_id")));
            user.setStatus(Status.valueOf(rs.getString("status")));
        } catch (SQLException e) {
            throw new EntityInitializationException(e);
        }
        return user;
    }

    @Override
    public void populateUpdateStatement(PreparedStatement statement, User user) {
        try {
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            statement.setLong(3, user.getRole().getDatabaseId());
            statement.setString(4, user.getStatus().name());
            statement.setLong(5, user.getId());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }

    private String getUserByVerificationTokenQuery() {
        return this.getQuery("get.user.by.verification.token");
    }

    @Override
    public String getQuery(String type) {
        return queryLoader.getQuery("query.user." + type);
    }
}
