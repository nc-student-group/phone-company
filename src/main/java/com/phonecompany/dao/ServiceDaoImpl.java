package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.ProductCategoryDao;
import com.phonecompany.dao.interfaces.ServiceDao;
import com.phonecompany.exception.EntityInitializationException;
import com.phonecompany.exception.EntityNotFoundException;
import com.phonecompany.exception.PreparedStatementPopulationException;
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

@SuppressWarnings("Duplicates")
@Repository
public class ServiceDaoImpl extends CrudDaoImpl<Service>
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

    @Override
    public List<Service> getByProductCategoryIdAndPaging(Long productCategoryId, int page, int size) {
        List<Service> services = new ArrayList<>();
        String getAllQuery = this.getQuery("getAll");
        if (productCategoryId != 0) {
            getAllQuery += " INNER JOIN product_category AS pc ON pc.id = s.product_category_id WHERE product_category_id = ?";
        }
        getAllQuery += " LIMIT ? OFFSET ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(getAllQuery)) {
            if (productCategoryId != 0) {
                ps.setLong(1, productCategoryId);
                ps.setInt(2, size);
                ps.setInt(3, page * size);
            } else {
                ps.setInt(1, size);
                ps.setInt(2, page * size);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                services.add(init(rs));
            }
        } catch (SQLException e) {
            throw new EntityNotFoundException(productCategoryId, e);
        }

        return services;
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
    public Integer getCountByProductCategoryIdAndPaging(long regionId) {
        String query = this.getQuery("getCount");
        if (regionId != 0) {
            query += " INNER JOIN product_category AS pc ON pc.id = s.prod_category_id WHERE prod_category_id = ? ";
        }
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            if (regionId != 0) {
                ps.setLong(1, regionId);
            }
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new EntityNotFoundException(regionId, e);
        }
    }
}
