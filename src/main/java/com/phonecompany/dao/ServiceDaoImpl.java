package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.ProductCategoryDao;
import com.phonecompany.dao.interfaces.ServiceDao;
import com.phonecompany.exception.dao_layer.EntityInitializationException;
import com.phonecompany.exception.dao_layer.EntityNotFoundException;
import com.phonecompany.exception.dao_layer.PreparedStatementPopulationException;
import com.phonecompany.model.Service;
import com.phonecompany.model.enums.ProductStatus;
import com.phonecompany.util.Query;
import com.phonecompany.util.interfaces.QueryLoader;
import com.phonecompany.util.TypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ServiceDaoImpl extends AbstractPageableDaoImpl<Service>
        implements ServiceDao {

    private QueryLoader queryLoader;
    public ProductCategoryDao productCategoryDao;

    private static final Logger LOG = LoggerFactory.getLogger(ServiceDaoImpl.class);

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
            preparedStatement.setString(7, entity.getPreviewDescription());
            preparedStatement.setString(8, entity.getDescription());
            preparedStatement.setObject(9, entity.getDurationInDays());
            preparedStatement.setObject(10, entity.getAmount());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }

    @Override
    public void populateUpdateStatement(PreparedStatement preparedStatement, Service service) {
        try {
            preparedStatement.setObject(1, TypeMapper.getNullableId(service.getProductCategory()));
            preparedStatement.setString(2, service.getServiceName());
            preparedStatement.setDouble(3, service.getPrice());
            preparedStatement.setString(4, service.getProductStatus().name());
            preparedStatement.setDouble(5, service.getDiscount());
            preparedStatement.setObject(6, service.getPictureUrl());
            preparedStatement.setObject(7, service.getPreviewDescription());
            preparedStatement.setObject(8, service.getDescription());
            preparedStatement.setObject(9, service.getDurationInDays());
            preparedStatement.setObject(10, service.getAmount());
            preparedStatement.setDouble(11, TypeMapper.getNullableId(service));
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
            service.setPreviewDescription(resultSet.getString("preview_description"));
            service.setDescription(resultSet.getString("description"));
            service.setDurationInDays((resultSet.getInt("duration_in_days")));
            service.setAmount((resultSet.getInt("amount")));
        } catch (SQLException e) {
            throw new EntityInitializationException(e);
        }
        return service;
    }

    @Override
    public void updateServiceStatus(long serviceId, ProductStatus productStatus) {
        this.executeUpdate(this.getQuery("updateStatus"), new Object[]{productStatus.name(), serviceId});
    }

    @Override
    public List<Service> getAllServicesSearch(Query query) {
        return this.executeForList(query.getQuery(), query.getPreparedStatementParams().toArray());
    }

    @Override
    public boolean isExist(Service service) {
        Connection conn = DataSourceUtils.getConnection(getDataSource());
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(this.getQuery("checkIfExists"));
            ps.setString(1, service.getServiceName());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return this.isResultSetNotEmpty(rs);
            }
        } catch (SQLException e) {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.getDataSource());
            throw new EntityNotFoundException(service.getServiceName(), e);
        } finally {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.getDataSource());
        }
        return false;
    }

    private boolean isResultSetNotEmpty(ResultSet rs)
            throws SQLException {
        long rowCount = rs.getLong(1);
        return rowCount != 0;
    }


    @Override
    public String prepareWhereClause(Object... args) {

        String where = "";
        int productCategoryId = (int) args[0];

        if (productCategoryId != 0) {
            where += " INNER JOIN product_category AS pc ON pc.id = s.prod_category_id " +
                    "WHERE prod_category_id = ?";
            this.preparedStatementParams.add(productCategoryId);
        }
        return where;
    }
}
