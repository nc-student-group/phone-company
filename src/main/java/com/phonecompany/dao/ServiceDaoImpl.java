package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.ProductCategoryDao;
import com.phonecompany.dao.interfaces.ServiceDao;
import com.phonecompany.exception.EntityInitializationException;
import com.phonecompany.exception.PreparedStatementPopulationException;
import com.phonecompany.model.Service;
import com.phonecompany.model.enums.ProductStatus;
import com.phonecompany.util.QueryLoader;
import com.phonecompany.util.TypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class ServiceDaoImpl extends CrudDaoImpl<Service> implements ServiceDao {

    private QueryLoader queryLoader;
    public ProductCategoryDao productCategoryDao;

    @Autowired
    public ServiceDaoImpl(QueryLoader queryLoader, ProductCategoryDao productCategoryDao) {
        this.queryLoader = queryLoader;
        this.productCategoryDao = productCategoryDao;
    }

    @Override
    public String getQuery(String type) {
        return queryLoader.getQuery("query.service." + type);
    }

    @Override
    public void populateSaveStatement(PreparedStatement preparedStatement, Service entity) {
        try {
            preparedStatement.setLong(1, TypeMapper.getNullableId(entity.getProductCategory()));
            preparedStatement.setString(2, entity.getServiceName());
            preparedStatement.setDouble(3, entity.getPrice());
            preparedStatement.setString(4, entity.getProductStatus().name());
            preparedStatement.setDouble(5, entity.getDiscount());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }

    @Override
    public void populateUpdateStatement(PreparedStatement preparedStatement, Service entity) {
        try {
            preparedStatement.setLong(2, TypeMapper.getNullableId(entity.getProductCategory()));
            preparedStatement.setString(3, entity.getServiceName());
            preparedStatement.setDouble(3, entity.getPrice());
            preparedStatement.setString(4, entity.getProductStatus().name());
            preparedStatement.setDouble(5, entity.getDiscount());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }

    @Override
    public Service init(ResultSet resultSet) {
        Service service = new Service();
        try {
            service.setId(resultSet.getLong("id"));
            service.setProductCategory(productCategoryDao.getById(resultSet.getLong("prod_category_id")));
            service.setServiceName(resultSet.getString("service_name"));
            service.setPrice(resultSet.getDouble("price"));
            service.setProductStatus(ProductStatus.valueOf(resultSet.getString("product_status")));
            service.setDiscount(resultSet.getInt("discount"));
        } catch (SQLException e) {
            throw new EntityInitializationException(e);
        }
        return service;
    }
}
