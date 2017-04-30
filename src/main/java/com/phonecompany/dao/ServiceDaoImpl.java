package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.ProductCategoryDao;
import com.phonecompany.dao.interfaces.ServiceDao;
import com.phonecompany.exception.*;
import com.phonecompany.model.Service;
import com.phonecompany.model.enums.ProductStatus;
import com.phonecompany.util.QueryLoader;
import com.phonecompany.util.TypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ServiceDaoImpl extends AbstractPageableDaoImpl<Service>
        implements ServiceDao {

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
            preparedStatement.setObject(1, TypeMapper.getNullableId(entity.getProductCategory()));
            preparedStatement.setString(2, entity.getServiceName());
            preparedStatement.setDouble(3, entity.getPrice());
            preparedStatement.setString(4, entity.getProductStatus().name());
            preparedStatement.setDouble(5, entity.getDiscount());
            preparedStatement.setString(6, entity.getPictureUrl());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }

    @Override
    public void populateUpdateStatement(PreparedStatement preparedStatement, Service entity) {
        try {
            preparedStatement.setLong(1, TypeMapper.getNullableId(entity.getProductCategory()));
            preparedStatement.setString(2, entity.getServiceName());
            preparedStatement.setDouble(3, entity.getPrice());
            preparedStatement.setString(4, entity.getProductStatus().name());
            preparedStatement.setDouble(5, entity.getDiscount());
            preparedStatement.setString(6, entity.getPictureUrl());
            preparedStatement.setDouble(7, TypeMapper.getNullableId(entity));
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
            service.setDiscount(resultSet.getDouble("discount"));
            service.setPictureUrl(resultSet.getString("picture_url"));
        } catch (SQLException e) {
            throw new EntityInitializationException(e);
        }
        return service;
    }

    @Override
    public void updateServiceStatus(long serviceId, ProductStatus productStatus) {
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(this.getQuery("updateStatus"))) {
            ps.setString(1, productStatus.name());
            ps.setLong(2, serviceId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EntityModificationException(serviceId, e);
        }
    }

    @Override
    public boolean isExist(Service service) {
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(this.getQuery("checkIfExists"))) {
            ps.setString(1, service.getServiceName());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return this.isResultSetNotEmpty(rs);
            }
        } catch (SQLException e) {
            throw new EntityNotFoundException(service.getServiceName(), e);
        }
        return false;
    }

    private boolean isResultSetNotEmpty(ResultSet rs)
            throws SQLException {
        long rowCount = rs.getLong(1);
        return rowCount != 0;
    }

    @Override
    public String getWhereClause(Object... args) {

        String where = "";
        long productCategoryId = (long) args[0];

        if (productCategoryId != 0) {
            where += " INNER JOIN product_category AS pc ON pc.id = s.prod_category_id " +
                    "WHERE prod_category_id = ?";
            this.preparedStatementParams.add(productCategoryId);
        }
        return where;
    }
}
