package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.AbstractPageableDao;
import com.phonecompany.exception.CrudException;
import com.phonecompany.model.DomainEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public abstract class AbstractPageableDaoImpl<T extends DomainEntity>
        extends CrudDaoImpl<T> implements AbstractPageableDao<T> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractPageableDaoImpl.class);

    List<Object> preparedStatementParams = new ArrayList<>();

    @Override
    public List<T> getPaging(int page, int size, Object... args) {

        String pagingQuery = this.getPagingQuery(page, size, args);

        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(pagingQuery)) {
            LOG.debug("Opening connection for paging");
            this.transferParamsToPreparedStatement(ps);

            LOG.debug("Executing paging query: {}", pagingQuery);
            ResultSet rs = ps.executeQuery();

            List<T> result = new ArrayList<>();
            while (rs.next()) {
                result.add(init(rs));
            }

            return result;

        } catch (SQLException e) {
            this.preparedStatementParams.clear();
            throw new CrudException("Failed to load all the entities. " +
                    "Check your database connection or whether sql query is right", e);
        }
    }

    private void transferParamsToPreparedStatement(PreparedStatement ps) throws SQLException {
        LOG.debug("Prepared statement parameters: {}", preparedStatementParams);
        for(int i = 0; i < preparedStatementParams.size(); i++) {
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

        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(countQuery)) {
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
            throw new CrudException("Failed to load all the entities. " +
                    "Check your database connection or whether sql query is right", e);
        }
    }

    public String getCountQuery(Object... args) {
        String getCountQuery = this.getQuery("getCount");
        getCountQuery += this.prepareWhereClause(args);

        return getCountQuery;
    }

    public abstract String prepareWhereClause(Object... args);
}
