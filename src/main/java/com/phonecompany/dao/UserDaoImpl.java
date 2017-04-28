package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.UserDao;
import com.phonecompany.exception.EntityInitializationException;
import com.phonecompany.exception.PreparedStatementPopulationException;
import com.phonecompany.model.User;
import com.phonecompany.model.enums.Status;
import com.phonecompany.util.QueryLoader;
import com.phonecompany.util.TypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

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
        LOG.debug("Query type: {}", type);
        return queryLoader.getQuery("query.user." + type);
    }

    @Override
    public String buildQueryByParams(String getEntityQuery, Map<String, Object> params) {
        String where = " WHERE dbu.role_id <> 4 and dbu.role_id <> 1";
        Integer limit = (Integer) params.getOrDefault("limit", null);
        Integer offset = (Integer) params.getOrDefault("offset", null);

        int roleId = (int) params.get("roleId");
        String status = (String) params.get("status");

        if (roleId > 0) {
            where += " and dbu.role_id = ?";
        }
        if (!status.equals("ALL")) {
            where += " and dbu.status = ?";
        }
        getEntityQuery += where;
        if(limit != null) {
            getEntityQuery += " LIMIT ?";
        }
        if(offset != null) {
            getEntityQuery += " OFFSET ?";
        }

        return getEntityQuery;
    }

    @Override
    public void setStatementParams(PreparedStatement ps, Map<String, Object> params) throws SQLException {
        int roleId = (int) params.get("roleId");
        String status = (String) params.get("status");
        Integer limit = (Integer) params.getOrDefault("limit", null);
        Integer offset = (Integer) params.getOrDefault("offset", null);

        int parameterIndex = 1;

        if (roleId > 0) {
            ps.setInt(parameterIndex, roleId);
            parameterIndex++;
        }
        if (!status.equals("ALL")) {
            ps.setString(parameterIndex, status);
            parameterIndex++;
        }
        if(limit != null) {
            ps.setInt(parameterIndex++, limit);
        }
        if(offset != null) {
            ps.setInt(parameterIndex, offset);
        }
    }
}
