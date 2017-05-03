package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.CustomerDao;
import com.phonecompany.dao.interfaces.CustomerServiceDao;
import com.phonecompany.dao.interfaces.ServiceDao;
import com.phonecompany.exception.EntityInitializationException;
import com.phonecompany.exception.PreparedStatementPopulationException;
import com.phonecompany.model.CustomerServiceDto;
import com.phonecompany.model.enums.CustomerProductStatus;
import com.phonecompany.util.QueryLoader;
import com.phonecompany.util.TypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class CustomerServiceDaoImpl extends CrudDaoImpl<CustomerServiceDto> implements CustomerServiceDao {

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
        return queryLoader.getQuery("query.customer_service."+type);
    }

    @Override
    public void populateSaveStatement(PreparedStatement preparedStatement, CustomerServiceDto entity) {
        try {
            preparedStatement.setObject(1, TypeMapper.getNullableId(entity.getCustomer()));
            preparedStatement.setObject(2, TypeMapper.getNullableId(entity.getService()));
            preparedStatement.setDouble(3, entity.getPrice());
            preparedStatement.setString(4, entity.getOrderStatus().name());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }

    @Override
    public void populateUpdateStatement(PreparedStatement preparedStatement, CustomerServiceDto entity) {
        try {
            preparedStatement.setObject(1, TypeMapper.getNullableId(entity.getCustomer()));
            preparedStatement.setObject(2, TypeMapper.getNullableId(entity.getService()));
            preparedStatement.setDouble(3, entity.getPrice());
            preparedStatement.setString(4, entity.getOrderStatus().name());
            preparedStatement.setLong(5, entity.getId());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }

    @Override
    public CustomerServiceDto init(ResultSet rs) {
        CustomerServiceDto customerService = new CustomerServiceDto();
        try {
            customerService.setId(rs.getLong("id"));
            customerService.setCustomer(customerDao.getById(rs.getLong("customer_id)")));
            customerService.setService(serviceDao.getById(rs.getLong("service_id)")));
            customerService.setPrice(rs.getDouble("price"));
            customerService.setOrderStatus(CustomerProductStatus.valueOf(rs.getString("service_status")));

        } catch (SQLException e) {
            throw new EntityInitializationException(e);
        }
        return customerService;
    }
}
