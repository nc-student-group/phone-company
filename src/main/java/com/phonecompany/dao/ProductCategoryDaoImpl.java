package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.ProductCategoryDao;
import com.phonecompany.model.ProductCategory;
import com.phonecompany.util.QueryLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by Oksanka on 21.04.2017.
 */
@Repository
public class ProductCategoryDaoImpl extends CrudDaoImpl<ProductCategory>
        implements ProductCategoryDao {

    private QueryLoader queryLoader;

    @Autowired
    public ProductCategoryDaoImpl(QueryLoader queryLoader) {
        this.queryLoader = queryLoader;
    }

    @Override
    public void populateSaveStatement(PreparedStatement statement, ProductCategory productCategory) {
        /*try {
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            statement.setLong(3, user.getRole().getDatabaseId());
            statement.setString(4, user.getStatus().name());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }*/
    }

    @Override
    public ProductCategory init(ResultSet rs) {
        ProductCategory productCategory = new ProductCategory();
        /*try {
            user.setId(rs.getLong("id"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setRole(TypeMapper.getUserRoleByDatabaseId(rs.getLong("role_id")));
            user.setStatus(Status.valueOf(rs.getString("status")));
        } catch (SQLException e) {
            throw new EntityInitializationException(e);
        }*/
        return productCategory;
    }

    @Override
    public void populateUpdateStatement(PreparedStatement statement, ProductCategory productCategory) {
        /*try {
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            statement.setLong(3, user.getRole().getDatabaseId());
            statement.setString(4, user.getStatus().name());
            statement.setLong(5, user.getId());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
        */
    }

    @Override
    public String getQuery(String type) {
        return queryLoader.getQuery("query.product_category." + type);
    }
}
