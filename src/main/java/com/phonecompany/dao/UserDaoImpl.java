package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.AddressDao;
import com.phonecompany.dao.interfaces.UserDao;
import com.phonecompany.exception.EntityInitializationException;
import com.phonecompany.exception.EntityNotFoundException;
import com.phonecompany.exception.EntityPersistenceException;
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
public class UserDaoImpl extends CrudDaoImpl<User>
        implements UserDao {

    @Value("${spring.datasource.url}")
    private String connStr;

    private QueryLoader queryLoader;
    private AddressDao addressDao;

    @Autowired
    public UserDaoImpl(QueryLoader queryLoader,
                       AddressDao addressDao) {
        this.queryLoader = queryLoader;
        this.addressDao = addressDao;
    }

    @Override
    public User findByEmail(String email) {
        try (Connection conn = DriverManager.getConnection(connStr);
             PreparedStatement ps = conn.prepareStatement(this.getQuery("getByEmail"))) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return init(rs);
            }
        } catch (SQLException e) {
            throw new EntityNotFoundException(-1l, e);
        }
        return null;
    }

    @Override
    public User getUserByVerificationToken(String token) {
        String userByVerificationTokenQuery = this.getUserByVerificationTokenQuery();
        try (Connection conn = DriverManager.getConnection(connStr);
             PreparedStatement ps = conn.prepareStatement(userByVerificationTokenQuery)) {
            ps.setString(1, token);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return this.init(rs);
        } catch (SQLException e) {
            throw new EntityNotFoundException(token, e);
        }
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
//            user.getRepresentative(representativeDao.getById( //TODO: set representative
//                    rs.getLong("representative_id")));
            user.setStatus(Status.valueOf(rs.getString("status")));
        } catch (SQLException e) {
            throw new EntityInitializationException(e);
        }
        return user;
    }

    @Override
    public void populateUpdateStatement(PreparedStatement preparedStatement, User user) {
    }

    private String getUserByVerificationTokenQuery() {
        return this.getQuery("get.user.by.verification.token");
    }

    @Override
    public String getQuery(String type) {
        return queryLoader.getQuery("query.user." + type);
    }
}
