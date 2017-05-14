package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.AbstractUserDao;
import com.phonecompany.exception.EntityModificationException;
import com.phonecompany.exception.EntityNotFoundException;
import com.phonecompany.model.User;
import com.phonecompany.model.enums.Status;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A base class that provides an abstract implementation for
 * all the methods applicable to any user in the system
 * (e.g. identification by email)
 *
 * @param <T> entity type
 */
public abstract class AbstractUserDaoImpl<T extends User>
        extends AbstractPageableDaoImpl<T> implements AbstractUserDao<T> {

    /**
     * Finds entity by its email
     *
     * @param email email to search by
     * @return entity found by email or {@literal null}
     * if none was found
     */
    @Override
    public T findByEmail(String email) {
        Connection conn = DataSourceUtils.getConnection(getDataSource());
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(this.getQuery("getByEmail"));
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return init(rs);
            }
        } catch (SQLException e) {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.getDataSource());
            throw new EntityNotFoundException(email, e);
        } finally {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.getDataSource());
        }
        return null;
    }

    @Override
    public void updateStatus(long id, Status status) {
        Connection conn = DataSourceUtils.getConnection(getDataSource());
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(this.getQuery("updateUserStatus"));
            ps.setString(1, status.name());
            ps.setLong(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.getDataSource());
            throw new EntityModificationException(id, e);
        } finally {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.getDataSource());
        }
    }

    @Override
    public int getCountByKey(String key, String countQuery) {
        Connection conn = DataSourceUtils.getConnection(getDataSource());
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(countQuery);
            ps.setString(1, key);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.getDataSource());
            throw new EntityNotFoundException(key, e);
        } finally {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.getDataSource());
        }
    }

    @Override
    public int getCountByEmail(String email) {
        return this.getCountByKey(email, this.getCountByEmailQuery());
    }

    private String getCountByEmailQuery() {
        return this.getQuery("count.by.email");
    }
}
