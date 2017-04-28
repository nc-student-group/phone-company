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
            preparedStatement.setLong(1, TypeMapper.getNullableId(entity.getProductCategory()));
            preparedStatement.setString(2, entity.getServiceName());
            preparedStatement.setDouble(3, entity.getPrice());
            preparedStatement.setString(4, entity.getProductStatus().name());
            preparedStatement.setDouble(5, entity.getDiscount());
            preparedStatement.setDouble(6, TypeMapper.getNullableId(entity));
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
        } catch (SQLException e) {
            throw new EntityInitializationException(e);
        }
        return service;
    }

    @Override
    public List<Service> getByProductCategoryIdAndPaging(Long productCategoryId,
                                                         int page, int size) {
        List<Object> params = new ArrayList<>();
        String query = buildQuery(this.getQuery("getAll"), params, productCategoryId);
        query += " LIMIT ? OFFSET ?";
        params.add(size);
        params.add(page * size);
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ResultSet rs = ps.executeQuery();
            List<Service> result = new ArrayList<>();
            while (rs.next()) {
                result.add(init(rs));
            }
            return result;
        } catch (SQLException e) {
            throw new CrudException("Failed to load all the entities. " +
                    "Check your database connection or whether sql query is right", e);
        }
    }

    private String buildQuery(String query, List params, Long productCategoryId) {
        if (productCategoryId != 0) {
            query += " INNER JOIN product_category AS pc ON pc.id = s.prod_category_id WHERE prod_category_id = ?";
            params.add(productCategoryId);
        }
        return query;
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
