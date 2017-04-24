package com.phonecompany.dao.interfaces;

import com.phonecompany.model.ProductCategory;

public interface ProductCategoryDao extends CrudDao<ProductCategory> {
    ProductCategory getByName(String productCategoryName);
}
