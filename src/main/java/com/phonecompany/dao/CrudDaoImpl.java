package com.phonecompany.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.phonecompany.dao.interfaces.CrudDao;
import com.phonecompany.exception.*;
import com.phonecompany.model.DomainEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A base class that provides an abstract implementation for
 * all the methods applicable to any entity in the system
 * (e.g. save or update)
 *
 * @param <T> entity type
 */
public abstract class CrudDaoImpl<T extends DomainEntity>
        implements CrudDao<T> {

    private static final Logger LOG = LoggerFactory.getLogger(CrudDaoImpl.class);

    @Autowired
    private ComboPooledDataSource dataSource;

    /**
     * {@inheritDoc}
     */
    @Override
    public T save(T entity) {
        Connection conn = DataSourceUtils.getConnection(this.dataSource);
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(this.getQuery("save"));
            LOG.debug("Saving entity: {}", entity);
            this.populateSaveStatement(ps, entity);
            ResultSet rs = ps.executeQuery();
            rs.next();
            long generatedId = rs.getLong(1);
            entity.setId(generatedId);
            return entity;
        } catch (SQLException e) {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.dataSource);
            throw new EntityPersistenceException(entity, e);
        } finally {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.dataSource);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T update(T entity) {
        LOG.debug("Getting query: {}", this.getQuery("update"));
        Connection conn = DataSourceUtils.getConnection(dataSource);
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(this.getQuery("update"));
            this.populateUpdateStatement(ps, entity);
            ps.executeUpdate();
        } catch (SQLException e) {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.dataSource);
            throw new EntityModificationException(entity, e);
        } finally {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.dataSource);
        }
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T getById(Long id) {
        Connection conn = DataSourceUtils.getConnection(dataSource);
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(this.getQuery("getById"));
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return init(rs);
            }
        } catch (SQLException e) {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.dataSource);
            throw new EntityNotFoundException(id, e);
        } finally {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.dataSource);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> getAll() {
        Connection conn = DataSourceUtils.getConnection(dataSource);
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(this.getQuery("getAll"));
            ResultSet rs = ps.executeQuery();
            List<T> result = new ArrayList<>();
            while (rs.next()) {
                result.add(init(rs));
            }
            return result;
        } catch (SQLException e) {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.dataSource);
            throw new CrudException("Failed to load all the entities. " +
                    "Check your database connection or whether sql query is right", e);
        } finally {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.dataSource);
        }

    }

    @Override
    public int getCountByKey(String key, String countQuery) {
        Connection conn = DataSourceUtils.getConnection(dataSource);
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
            DataSourceUtils.releaseConnection(conn, this.dataSource);
            throw new EntityNotFoundException(key, e);
        } finally {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.dataSource);
        }
    }

    @Override
    public List<T> executeForList(String query, Object[] params) {
        Connection conn = DataSourceUtils.getConnection(dataSource);
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ResultSet rs = ps.executeQuery();
            List<T> result = new ArrayList<>();
            while (rs.next()) {
                result.add(init(rs));
            }
            return result;
        } catch (SQLException e) {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.dataSource);
            throw new CrudException("Failed to load all the entities. " +
                    "Check your database connection or whether sql query is right", e);
        } finally {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.dataSource);
        }
    }

    @Override
    public T executeForObject(String query, Object[] params) {
        Connection conn = DataSourceUtils.getConnection(dataSource);
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return init(rs);
            }
        } catch (SQLException e) {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.dataSource);
            throw new CrudException("Failed to load all the entities. " +
                    "Check your database connection or whether sql query is right", e);
        } finally {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.dataSource);
        }
        return null;
    }

    @Override
    public void executeUpdate(String query, Object[] params) {
        Connection conn = DataSourceUtils.getConnection(getDataSource());
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.getDataSource());
            throw new EntityModificationException(e);
        } finally {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.getDataSource());
        }
    }

    @Override
    public int executeForInt(String query, Object[] params) {
        Connection conn = DataSourceUtils.getConnection(getDataSource());
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.getDataSource());
            throw new EntityModificationException(e);
        } finally {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.getDataSource());
        }
    }

    public abstract String getQuery(String type);

    public abstract void populateSaveStatement(PreparedStatement preparedStatement, T entity);

    public abstract void populateUpdateStatement(PreparedStatement preparedStatement, T entity);

    public abstract T init(ResultSet resultSet);

    public ComboPooledDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(ComboPooledDataSource dataSource) {
        this.dataSource = dataSource;
    }
}
