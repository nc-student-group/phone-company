package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.CorporateDao;
import com.phonecompany.exception.EntityInitializationException;
import com.phonecompany.exception.PreparedStatementPopulationException;
import com.phonecompany.model.Corporate;
import com.phonecompany.util.QueryLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class CorporateDaoImpl extends AbstractPageableDaoImpl<Corporate> implements CorporateDao {

    private QueryLoader queryLoader;
    private String partOfName;

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

    @Override
    public String prepareWhereClause(Object... args) {
        String where = "";
        String partOfName = (String) args[0];
        where +=  " WHERE corporate_name LIKE CONCAT('%',?,'%')";
        this.preparedStatementParams.add(partOfName);
        return where;
    }
}
