package com.phonecompany.dao.interfaces;

import com.phonecompany.model.ProductCategory;

public interface ProductCategoryDao extends JdbcOperations<ProductCategory> {
    ProductCategory getByName(String productCategoryName);
}
