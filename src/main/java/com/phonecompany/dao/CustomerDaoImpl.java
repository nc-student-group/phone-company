package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.AddressDao;
import com.phonecompany.dao.interfaces.CorporateDao;
import com.phonecompany.dao.interfaces.CustomerDao;
import com.phonecompany.model.Customer;
import com.phonecompany.util.QueryLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class CustomerDaoImpl extends CrudDaoImpl<Customer>
        implements CustomerDao{

    private QueryLoader queryLoader;
    private AddressDao addressDao;
    private CorporateDao corporateDao;

    @Autowired
    public CustomerDaoImpl(QueryLoader queryLoader, AddressDao addressDao, CorporateDao corporateDao) {
        this.queryLoader = queryLoader;
        this.addressDao = addressDao;
        this.corporateDao = corporateDao;
    }

    @Override
    public String getQuery(String type) {
        return null;
    }

    @Override
    public void populateSaveStatement(PreparedStatement preparedStatement, Customer entity) {

    }

    @Override
    public void populateUpdateStatement(PreparedStatement preparedStatement, Customer entity) {

    }

    @Override
    public Customer init(ResultSet resultSet) {
        return null;
    }
}
