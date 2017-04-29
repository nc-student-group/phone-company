package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.AbstractPageableDao;
import com.phonecompany.exception.CrudException;
import com.phonecompany.model.DomainEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPageableDaoImpl<T extends DomainEntity>
        extends CrudDaoImpl<T> implements AbstractPageableDao<T> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractPageableDaoImpl.class);

    @Override
    public List<T> getPaging(int page, int size, Object... args) {

        String pagingQuery = this.getPagingQuery(page, size, args);

        try (Connection conn = dbManager.getConnection();
             Statement s = conn.createStatement()) {

            LOG.debug("Executing paging query: {}", pagingQuery);
            ResultSet rs = s.executeQuery(pagingQuery);

            List<T> result = new ArrayList<>();
            while (rs.next()) {
                result.add(init(rs));
            }
            return result;

        } catch (SQLException e) {
            throw new CrudException("Failed to load all the entities. " +
                    "Check your database connection or whether sql query is right", e);
        }
    }

    public String getPagingQuery(int page, int size, Object... args) {

        String getEntityQuery = this.getQuery("getAll");
        String whereClause = this.getWhereClause(args);

        whereClause += " LIMIT " + size;
        whereClause += " OFFSET " + page * size;

        getEntityQuery += whereClause;

        return getEntityQuery;
    }

    public int getEntityCount(Object... args) {

        String countQuery = this.getCountQuery(args);

        try (Connection conn = dbManager.getConnection();
             Statement s = conn.createStatement()) {

            LOG.debug("Executing count query: {}", countQuery);
            ResultSet rs = s.executeQuery(countQuery);

            if (rs.next()) {
                return rs.getInt(1);
            }

            return 0;
        } catch (SQLException e) {
            throw new CrudException("Failed to load all the entities. " +
                    "Check your database connection or whether sql query is right", e);
        }
    }

    public abstract String getWhereClause(Object... args);

    public abstract String getCountQuery(Object... args);
}
