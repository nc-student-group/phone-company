package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.CustomerDao;
import com.phonecompany.dao.interfaces.CustomerServiceDao;
import com.phonecompany.dao.interfaces.ServiceDao;
import com.phonecompany.exception.EntityInitializationException;
import com.phonecompany.exception.PreparedStatementPopulationException;
import com.phonecompany.model.CustomerService;
import com.phonecompany.model.enums.OrderStatus;
import com.phonecompany.util.QueryLoader;
import com.phonecompany.util.TypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        return queryLoader.getQuery("query.customer_service."+type);
    }

    @Override
    public void populateSaveStatement(PreparedStatement preparedStatement, CustomerService entity) {
        try {
            preparedStatement.setLong(1, TypeMapper.getNullableId(entity.getCustomer()));
            preparedStatement.setLong(2, TypeMapper.getNullableId(entity.getService()));
            preparedStatement.setDate(3, entity.getOrderDate());
            preparedStatement.setDouble(4, entity.getPrice());
            preparedStatement.setString(5, entity.getOrderStatus().name());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }

    @Override
    public void populateUpdateStatement(PreparedStatement preparedStatement, CustomerService entity) {
        try {
            preparedStatement.setLong(1, TypeMapper.getNullableId(entity.getCustomer()));
            preparedStatement.setLong(2, TypeMapper.getNullableId(entity.getService()));
            preparedStatement.setDate(3, entity.getOrderDate());
            preparedStatement.setDouble(4, entity.getPrice());
            preparedStatement.setString(5, entity.getOrderStatus().name());
            preparedStatement.setLong(6, entity.getId());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }

    @Override
    public CustomerService init(ResultSet rs) {
        CustomerService customerService = new CustomerService();
        try {
            customerService.setId(rs.getLong("id"));
            customerService.setCustomer(customerDao.getById(rs.getLong("customer_id)")));
            customerService.setService(serviceDao.getById(rs.getLong("service_id)")));
            customerService.setOrderDate(rs.getDate("order_date"));
            customerService.setPrice(rs.getDouble("price"));
            customerService.setOrderStatus(OrderStatus.valueOf(rs.getString("order_status")));

        } catch (SQLException e) {
            throw new EntityInitializationException(e);
        }
        return customerService;
    }
}
