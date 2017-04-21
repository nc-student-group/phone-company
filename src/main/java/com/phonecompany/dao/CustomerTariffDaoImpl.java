package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.CorporateDao;
import com.phonecompany.dao.interfaces.CustomerDao;
import com.phonecompany.dao.interfaces.CustomerTariffDao;
import com.phonecompany.dao.interfaces.TariffDao;
import com.phonecompany.model.CustomerTariff;
import com.phonecompany.util.QueryLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class CustomerTariffDaoImpl extends CrudDaoImpl<CustomerTariff> implements CustomerTariffDao {

    private QueryLoader queryLoader;
    private CustomerDao customerDao;
    private CorporateDao corporateDao;
    private TariffDao tariffDao;

    @Autowired
    public CustomerTariffDaoImpl(QueryLoader queryLoader, CustomerDao customerDao, CorporateDao corporateDao, TariffDao tariffDao) {
        this.queryLoader = queryLoader;
        this.customerDao = customerDao;
        this.corporateDao = corporateDao;
        this.tariffDao = tariffDao;
    }

    @Override
    public String getQuery(String type) {
        return null;
    }

    @Override
    public void populateSaveStatement(PreparedStatement preparedStatement, CustomerTariff entity) {

    }

    @Override
    public void populateUpdateStatement(PreparedStatement preparedStatement, CustomerTariff entity) {

    }

    @Override
    public CustomerTariff init(ResultSet resultSet) {
        return null;
    }
}
