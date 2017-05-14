package com.phonecompany.service;

import com.phonecompany.dao.interfaces.ProductCategoryDao;
import com.phonecompany.model.ProductCategory;
import com.phonecompany.service.interfaces.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductCategoryServiceImpl extends CrudServiceImpl<ProductCategory>
        implements ProductCategoryService {

    private ProductCategoryDao productCategoryDao;

    @Autowired
    public ProductCategoryServiceImpl(ProductCategoryDao productCategoryDao){
        super(productCategoryDao);
        this.productCategoryDao = productCategoryDao;
    }
}
