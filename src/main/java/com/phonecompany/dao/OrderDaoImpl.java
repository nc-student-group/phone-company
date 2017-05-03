package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.CustomerServiceDao;
import com.phonecompany.dao.interfaces.CustomerTariffDao;
import com.phonecompany.dao.interfaces.OrderDao;
import com.phonecompany.exception.EntityInitializationException;
import com.phonecompany.exception.EntityNotFoundException;
import com.phonecompany.exception.PreparedStatementPopulationException;
import com.phonecompany.model.Order;
import com.phonecompany.model.enums.OrderStatus;
import com.phonecompany.model.enums.OrderType;
import com.phonecompany.util.QueryLoader;
import com.phonecompany.util.TypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.phonecompany.util.TypeMapper.toLocalDate;
import static com.phonecompany.util.TypeMapper.toSqlDate;

@Repository
public class OrderDaoImpl extends CrudDaoImpl<Order> implements OrderDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderDaoImpl.class);
    private QueryLoader queryLoader;
    private CustomerServiceDao customerServiceDao;
    private CustomerTariffDao customerTariffDao;

    @Autowired
    public OrderDaoImpl(QueryLoader queryLoader, CustomerServiceDao customerServiceDao, CustomerTariffDao customerTariffDao) {
        this.queryLoader = queryLoader;
        this.customerServiceDao = customerServiceDao;
        this.customerTariffDao = customerTariffDao;
    }

    @Override
    public String getQuery(String type) {
        return queryLoader.getQuery("query.order." + type);
    }

    @Override
    public void populateSaveStatement(PreparedStatement preparedStatement, Order entity) {
        try {
            preparedStatement.setObject(1, TypeMapper.getNullableId(entity.getCustomerService()));
            preparedStatement.setObject(2, TypeMapper.getNullableId(entity.getCustomerTariff()));
            preparedStatement.setString(3, entity.getType().name());
            preparedStatement.setString(4, entity.getOrderStatus().name());
            preparedStatement.setDate(5, toSqlDate(entity.getCreationDate()));
            preparedStatement.setDate(6, toSqlDate(entity.getExecutionDate()));
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }

    @Override
    public void populateUpdateStatement(PreparedStatement preparedStatement, Order entity) {
        try {
            preparedStatement.setObject(1, TypeMapper.getNullableId(entity.getCustomerService()));
            preparedStatement.setObject(2, TypeMapper.getNullableId(entity.getCustomerTariff()));
            preparedStatement.setString(3, entity.getType().name());
            preparedStatement.setString(4, entity.getOrderStatus().name());
            preparedStatement.setDate(5, toSqlDate(entity.getCreationDate()));
            preparedStatement.setDate(6, toSqlDate(entity.getExecutionDate()));

            preparedStatement.setLong(7, entity.getId());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }

    @Override
    public Order init(ResultSet rs) {
        Order order = new Order();
        try {
            order.setId(rs.getLong("id"));
            order.setCustomerService(customerServiceDao.getById(rs.getLong("customer_service_id")));
            order.setCustomerTariff(customerTariffDao.getById(rs.getLong("customer_tariff_id")));
            order.setType(OrderType.valueOf(rs.getString("type")));
            order.setOrderStatus(OrderStatus.valueOf(rs.getString("order_status")));
            order.setCreationDate(toLocalDate(rs.getDate("creation_date")));
            order.setExecutionDate(toLocalDate(rs.getDate("execution_date")));

        } catch (SQLException e) {
            throw new EntityInitializationException(e);
        }
        return order;
    }

    @Override
    public List<Order> getResumingOrderByCustomerTariffId(Long customerTariffId) {
        String query = this.getQuery("getResumingByCustomerId");
        List<Order> orders = new ArrayList<>();
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, customerTariffId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                orders.add(init(rs));
            }
        } catch (SQLException e) {
            throw new EntityNotFoundException(customerTariffId, e);
        }
        return orders;
    }
}
