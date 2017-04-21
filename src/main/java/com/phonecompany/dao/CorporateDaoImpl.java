package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.CorporateDao;
import com.phonecompany.model.Corporate;
import com.phonecompany.util.QueryLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class CorporateDaoImpl extends CrudDaoImpl<Corporate> implements CorporateDao {

    private QueryLoader queryLoader;

    @Autowired
    public CorporateDaoImpl(QueryLoader queryLoader) {
        this.queryLoader = queryLoader;
    }

    @Override
    public String getQuery(String type) {
        return null;
    }

    @Override
    public void populateSaveStatement(PreparedStatement preparedStatement, Corporate entity) {

    }

    @Override
    public void populateUpdateStatement(PreparedStatement preparedStatement, Corporate entity) {

    }

    @Override
    public Corporate init(ResultSet resultSet) {
        return null;
    }
}
