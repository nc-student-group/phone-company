package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.UserDao;
import com.phonecompany.exception.CrudException;
import com.phonecompany.exception.EntityInitializationException;
import com.phonecompany.exception.PreparedStatementPopulationException;
import com.phonecompany.model.User;
import com.phonecompany.model.enums.Status;
import com.phonecompany.util.Query;
import com.phonecompany.util.interfaces.QueryLoader;
import com.phonecompany.util.TypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDaoImpl extends AbstractUserDaoImpl<User>
        implements UserDao {

    private QueryLoader queryLoader;

    private static final Logger LOG = LoggerFactory.getLogger(UserDaoImpl.class);

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

    @Override
    public String getQuery(String type) {
        return queryLoader.getQuery("query.user." + type);
    }

    @Override
    public String prepareWhereClause(Object... args) {

        String where = "";

        int roleId = (int) args[0];
        String status = (String) args[1];

        if (roleId > 0) {
            where += " AND role_id = ?";
            this.preparedStatementParams.add(roleId);
        }
        if (!status.equals("ALL")) {
            where += " AND status = ?";
            this.preparedStatementParams.add(status);
        }
        return where;
    }

    @Override
    public List<User> getAllUsersSearch(Query query) {
        Connection conn = DataSourceUtils.getConnection(this.getDataSource());
        PreparedStatement ps = null;

        try {
            LOG.info("Execute query: " + query.getQuery());
            ps = conn.prepareStatement(query.getQuery());

            for(int i = 0; i<query.getPreparedStatementParams().size();i++){
                ps.setObject(i+1,query.getPreparedStatementParams().get(i));
            }
            ResultSet rs = ps.executeQuery();
            List<User> result = new ArrayList<>();
            while (rs.next()) {
                result.add(init(rs));
            }
            return result;
        } catch (SQLException e) {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.getDataSource());
            throw new CrudException("Failed to load all the entities. " +
                    "Check your database connection or whether sql query is right", e);
        } finally {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.getDataSource());
        }
    }
}
