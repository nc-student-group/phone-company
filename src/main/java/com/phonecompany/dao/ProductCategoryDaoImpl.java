package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.ProductCategoryDao;
import com.phonecompany.exception.EntityInitializationException;
import com.phonecompany.exception.EntityNotFoundException;
import com.phonecompany.exception.PreparedStatementPopulationException;
import com.phonecompany.model.ProductCategory;
import com.phonecompany.util.interfaces.QueryLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;

import java.sql.*;

@SuppressWarnings("Duplicates")
@Repository
public class ProductCategoryDaoImpl extends JdbcOperationsImpl<ProductCategory>
        implements ProductCategoryDao {

    private QueryLoader queryLoader;

    @Autowired
    public ProductCategoryDaoImpl(QueryLoader queryLoader) {
        this.queryLoader = queryLoader;
    }

    @Override
    public String getQuery(String type) {
        return queryLoader.getQuery("query.product_category." + type);
    }

    @Override
    public void populateSaveStatement(PreparedStatement statement, ProductCategory productCategory) {
        try {
            statement.setString(1, productCategory.getCategoryName());
            statement.setString(1, productCategory.getUnits());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }

    @Override
    public ProductCategory init(ResultSet rs) {
        ProductCategory productCategory = new ProductCategory();
        try {
            productCategory.setId(rs.getLong("id"));
            productCategory.setCategoryName(rs.getString("category_name"));
            productCategory.setUnits(rs.getString("units"));
        } catch (SQLException e) {
            throw new EntityInitializationException(e);
        }
        return productCategory;
    }

    @Override
    public void populateUpdateStatement(PreparedStatement statement, ProductCategory productCategory) {
        try {
            statement.setString(1, productCategory.getCategoryName());
            statement.setString(2, productCategory.getUnits());
            statement.setLong(3, productCategory.getId());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }

    @Override
    public ProductCategory getByName(String productCategoryName) {
        return this.executeForObject(this.getQuery("getByName"), new Object[]{productCategoryName});
    }
}
