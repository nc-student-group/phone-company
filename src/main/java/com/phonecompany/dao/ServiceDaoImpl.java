package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.ProductCategoryDao;
import com.phonecompany.dao.interfaces.ServiceDao;
import com.phonecompany.model.Service;
import com.phonecompany.util.QueryLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class ServiceDaoImpl extends CrudDaoImpl<Service> implements ServiceDao {

    private QueryLoader queryLoader;
    private ProductCategoryDao productCategoryDao;

    @Autowired
    public ServiceDaoImpl(QueryLoader queryLoader, ProductCategoryDao productCategoryDao) {
        this.queryLoader = queryLoader;
        this.productCategoryDao = productCategoryDao;
    }

    @Override
    public String getQuery(String type) {
        return null;
    }

    @Override
    public void populateSaveStatement(PreparedStatement preparedStatement, Service entity) {

    }

    @Override
    public void populateUpdateStatement(PreparedStatement preparedStatement, Service entity) {

    }

    @Override
    public Service init(ResultSet resultSet) {
        return null;
    }
}
