package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.CustomerDao;
import com.phonecompany.dao.interfaces.CustomerServiceDao;
import com.phonecompany.dao.interfaces.ServiceDao;
import com.phonecompany.model.CustomerService;
import com.phonecompany.util.QueryLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class CustomerServiceDaoImpl extends CrudDaoImpl<CustomerService> implements CustomerServiceDao {

    private QueryLoader queryLoader;
    private CustomerDao customerDao;
    private ServiceDao serviceDao;

    @Autowired
    public CustomerServiceDaoImpl(QueryLoader queryLoader, CustomerDao customerDao, ServiceDao serviceDao) {
        this.queryLoader = queryLoader;
        this.customerDao = customerDao;
        this.serviceDao = serviceDao;
    }

    @Override
    public String getQuery(String type) {
        return null;
    }

    @Override
    public void populateSaveStatement(PreparedStatement preparedStatement, CustomerService entity) {

    }

    @Override
    public void populateUpdateStatement(PreparedStatement preparedStatement, CustomerService entity) {

    }

    @Override
    public CustomerService init(ResultSet resultSet) {
        return null;
    }
}
