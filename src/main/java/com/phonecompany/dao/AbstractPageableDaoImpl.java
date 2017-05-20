package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.AbstractPageableDao;
import com.phonecompany.exception.CrudException;
import com.phonecompany.model.DomainEntity;
import com.phonecompany.util.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPageableDaoImpl<T extends DomainEntity>
        extends JdbcOperationsImpl<T> implements AbstractPageableDao<T> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractPageableDaoImpl.class);

    List<Object> preparedStatementParams = new ArrayList<>();

    @Override
    public List<T> getPaging(int page, int size, Object... args) {

        String pagingQuery = this.getPagingQuery(page, size, args);
        Connection conn = DataSourceUtils.getConnection(getDataSource());
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(pagingQuery);
            LOG.debug("Opening connection for isPaging");
            this.transferParamsToPreparedStatement(ps);

            LOG.debug("Executing isPaging query: {}", pagingQuery);
            ResultSet rs = ps.executeQuery();

            List<T> result = new ArrayList<>();
            while (rs.next()) {
                result.add(init(rs));
            }
            return result;

        } catch (SQLException e) {
            this.preparedStatementParams.clear();
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, getDataSource());
            throw new CrudException("Failed to load all the entities. " +
                    "Check your database connection or whether sql query is right", e);
        } finally {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, getDataSource());
        }
    }

    private void transferParamsToPreparedStatement(PreparedStatement ps) throws SQLException {
        LOG.debug("Prepared statement parameters: {}", preparedStatementParams);
        for (int i = 0; i < preparedStatementParams.size(); i++) {
            ps.setObject(i + 1, preparedStatementParams.get(i));
        }
        this.preparedStatementParams.clear();
    }

    public String getPagingQuery(int page, int size, Object... args) {

        String getAllQuery = this.getQuery("getAll");
        String whereClause = this.prepareWhereClause(args);

        whereClause += " LIMIT ?";
        this.preparedStatementParams.add(size);
        whereClause += " OFFSET ?";
        this.preparedStatementParams.add(size * page);

        getAllQuery += whereClause;

        return getAllQuery;
    }

    public int getEntityCount(Object... args) {

        String countQuery = this.getCountQuery(args);
        Connection conn = DataSourceUtils.getConnection(getDataSource());
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(countQuery);
            LOG.debug("Opening connection for count");

            this.transferParamsToPreparedStatement(ps);

            LOG.debug("Executing count query: {}", countQuery);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            this.preparedStatementParams.clear();
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.getDataSource());
            throw new CrudException("Failed to load all the entities. " +
                    "Check your database connection or whether sql query is right", e);
        }finally {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.getDataSource());
        }
    }

    public String getCountQuery(Object... args) {
        String getCountQuery = this.getQuery("getCount");
        getCountQuery += this.prepareWhereClause(args);

        return getCountQuery;
    }

    public List<T> getByQuery(Query query) {
        Connection conn = DataSourceUtils.getConnection(this.getDataSource());
        PreparedStatement ps = null;
        LOG.info("Execute query: " + query.getQuery());
        try {
            ps = conn.prepareStatement(query.getQuery());

            for (int i = 0; i < query.getPreparedStatementParams().size(); i++) {
                ps.setObject(i + 1, query.getPreparedStatementParams().get(i));
            }
            ResultSet rs = ps.executeQuery();
            List<T> result = new ArrayList<>();
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

    public int getCountByQuery(Query query) {
        Connection conn = DataSourceUtils.getConnection(this.getDataSource());
        PreparedStatement ps = null;
        LOG.info("Execute query: " + query.getCountQuery());
        try {
            ps = conn.prepareStatement(query.getCountQuery());

            for (int i = 0; i < query.getCountParams().size(); i++) {
                ps.setObject(i + 1, query.getCountParams().get(i));
            }
            ResultSet rs = ps.executeQuery();
            List<T> result = new ArrayList<>();
            while (rs.next()) {
                result.add(init(rs));
            }
            return result.size();
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

    public abstract String prepareWhereClause(Object... args);
}
