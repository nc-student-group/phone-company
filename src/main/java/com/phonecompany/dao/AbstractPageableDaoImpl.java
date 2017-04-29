package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.AbstractPageableDao;
import com.phonecompany.exception.CrudException;
import com.phonecompany.model.DomainEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractPageableDaoImpl<T extends DomainEntity>
        extends CrudDaoImpl<T> implements AbstractPageableDao<T> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractPageableDaoImpl.class);

    @Override
    public List<T> getPagingByParametersMap(int page, int size, Map<String, Object> params) {

        String getEntityQuery = this.getQuery("getEntity");
        String paginationQuery = this.buildQueryByParams(getEntityQuery, params);
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(paginationQuery)) {

            this.setStatementParams(ps, params);

            ResultSet rs = ps.executeQuery();
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

    public int getEntityCount(Map<String, Object> params) {
        String countQuery = this.getQuery("getCount");
        LOG.debug("Count query: {}", countQuery);
        String parametrizedCountQuery = this.buildQueryByParams(countQuery, params);
        LOG.debug("Parametrized Count query: {}", parametrizedCountQuery);
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(parametrizedCountQuery)) {
            this.setStatementParams(ps, params);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }

            return 0;
        } catch (SQLException e) {
            throw new CrudException("Failed to load all the entities. " +
                    "Check your database connection or whether sql query is right", e);
        }
    }

    public abstract String buildQueryByParams(String getEntityQuery, Map<String, Object> params);
    public abstract void setStatementParams(PreparedStatement ps, Map<String, Object> params) throws SQLException;
}
