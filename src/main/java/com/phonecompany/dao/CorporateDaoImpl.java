package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.CorporateDao;
import com.phonecompany.exception.dao_layer.EntityInitializationException;
import com.phonecompany.exception.dao_layer.PreparedStatementPopulationException;
import com.phonecompany.model.Corporate;
import com.phonecompany.util.interfaces.QueryLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class CorporateDaoImpl extends JdbcOperationsImpl<Corporate> implements CorporateDao {

    private QueryLoader queryLoader;

    @Autowired
    public CorporateDaoImpl(QueryLoader queryLoader) {
        this.queryLoader = queryLoader;
    }

    @Override
    public String getQuery(String type) {
        return queryLoader.getQuery("query.corporate." + type);
    }

    @Override
    public void populateSaveStatement(PreparedStatement preparedStatement, Corporate entity) {
        try {
            preparedStatement.setString(1, entity.getCorporateName());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }

    @Override
    public void populateUpdateStatement(PreparedStatement preparedStatement, Corporate entity) {
        try {
            preparedStatement.setString(1, entity.getCorporateName());

            preparedStatement.setLong(2, entity.getId());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }

    @Override
    public Corporate init(ResultSet rs) {
        Corporate corporate = new Corporate();
        try {
            corporate.setId(rs.getLong("id"));
            corporate.setCorporateName(rs.getString("corporate_name"));
        } catch (SQLException e) {
            throw new EntityInitializationException(e);
        }
        return corporate;
    }

}
