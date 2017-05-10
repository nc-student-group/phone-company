package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.CustomerServiceDao;
import com.phonecompany.dao.interfaces.CustomerTariffDao;
import com.phonecompany.dao.interfaces.OrderDao;
import com.phonecompany.exception.CrudException;
import com.phonecompany.exception.EntityInitializationException;
import com.phonecompany.exception.EntityNotFoundException;
import com.phonecompany.exception.PreparedStatementPopulationException;
import com.phonecompany.model.Order;
import com.phonecompany.model.enums.OrderStatus;
import com.phonecompany.model.enums.OrderType;
import com.phonecompany.model.enums.WeekOfMonth;
import com.phonecompany.model.proxy.DynamicProxy;
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
import java.util.*;

import static com.phonecompany.model.proxy.SourceMappers.CUSTOMER_SERVICE_MAPPER;
import static com.phonecompany.model.proxy.SourceMappers.CUSTOMER_TARIFF_MAPPER;
import static com.phonecompany.util.TypeMapper.toLocalDate;
import static com.phonecompany.util.TypeMapper.toSqlDate;

@Repository
public class OrderDaoImpl extends CrudDaoImpl<Order> implements OrderDao {

    private static final Logger LOG = LoggerFactory.getLogger(OrderDaoImpl.class);

    private QueryLoader queryLoader;
    private CustomerServiceDao customerServiceDao;
    private CustomerTariffDao customerTariffDao;

    @Autowired
    public OrderDaoImpl(QueryLoader queryLoader,
                        CustomerServiceDao customerServiceDao,
                        CustomerTariffDao customerTariffDao) {
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
            long customerServiceId = rs.getLong("customer_service_id");
            order.setCustomerService(DynamicProxy.newInstance(customerServiceId, CUSTOMER_SERVICE_MAPPER));
            long customerTariffId = rs.getLong("customer_tariff_id");
            order.setCustomerTariff(DynamicProxy.newInstance(customerTariffId, CUSTOMER_TARIFF_MAPPER));
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
        List<Order> orders = new ArrayList<>();
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(this.getQuery("getResumingByCustomerId"))) {
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

    @Override
    public List<Order> getResumingOrderByCustomerServiceId(Long customerId) {
        List<Order> orders = new ArrayList<>();
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(this.getQuery("getResumingServicesByCustomerId"))) {
            ps.setLong(1, customerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                orders.add(init(rs));
            }
        } catch (SQLException e) {
            throw new EntityNotFoundException(customerId, e);
        }
        return orders;
    }

    @Override
    public List<Order> getOrdersByCustomerIdPaged(Long customerId, int page, int size) {
        String query = this.getQuery("getPagedByCustomerId");
        return getOrdersByClientIdPaged(query, customerId, page, size);
    }

    @Override
    public List<Order> getOrdersByCorporateIdPaged(Long corporate, int page, int size) {
        String query = this.getQuery("getPagedByCorporateId");
        return getOrdersByClientIdPaged(query, corporate, page, size);
    }

    @Override
    public Integer getCountByCustomerId(Long customerId) {
        String query = this.getQuery("getCountByCustomerId");
        return getCountByClientId(query, customerId);
    }

    @Override
    public Integer getCountByCorporateId(Long corporateId) {
        String query = this.getQuery("getCountByCorporateId");
        return getCountByClientId(query, corporateId);
    }

    private List<Order> getOrdersByClientIdPaged(String query, Long clientId, int page, int size) {
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setObject(1, clientId);
            ps.setObject(2, size);
            ps.setObject(3, page * size);
            ResultSet rs = ps.executeQuery();
            List<Order> result = new ArrayList<>();
            while (rs.next()) {
                result.add(init(rs));
            }
            return result;
        } catch (SQLException e) {
            throw new CrudException("Failed to load all the entities. " +
                    "Check your database connection or whether sql query is right", e);
        }
    }

    private Integer getCountByClientId(String query, Long clientId) {
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, clientId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new EntityNotFoundException(clientId, e);
        }
    }

    @Override
    public List<Order> getOrdersForCustomerServicesByCustomerIdPaged(Long customerId, int page, int size) {
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(this.getQuery("getPagedForCustomerServicesByCustomerId"))) {
            ps.setObject(1, customerId);
            ps.setObject(2, size);
            ps.setObject(3, page * size);
            ResultSet rs = ps.executeQuery();
            List<Order> result = new ArrayList<>();
            while (rs.next()) {
                result.add(init(rs));
            }
            return result;
        } catch (SQLException e) {
            throw new CrudException("Failed to load all the entities. " +
                    "Check your database connection or whether sql query is right", e);
        }
    }

    @Override
    public Integer getCountOfServicesByCustomerId(Long customerId) {
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(this.getQuery("getCountOfServicesByCustomerId"))) {
            ps.setLong(1, customerId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new EntityNotFoundException(customerId, e);
        }
    }

    @Override
    public List<Order> getTariffOrdersByRegionId(long regionId) {
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(this.getQuery("tariff.by.region.id"))) {
            ps.setLong(1, regionId);
            ResultSet rs = ps.executeQuery();
            List<Order> tariffOrders = new ArrayList<>();
            while (rs.next()) {
                tariffOrders.add(this.init(rs));
            }
            return tariffOrders;
        } catch (SQLException e) {
            throw new EntityNotFoundException(regionId, e);
        }
    }

    @Override
    public List<Order> getAllServiceOrders() {
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(this.getQuery("services"))) {
            ResultSet rs = ps.executeQuery();
            List<Order> serviceOrders = new ArrayList<>();
            while (rs.next()) {
                serviceOrders.add(this.init(rs));
            }
            return serviceOrders;
        } catch (SQLException e) {
            throw new CrudException("Could not extract service orders", e);
        }
    }

    @Override
    public EnumMap<WeekOfMonth, Integer> getNumberOfOrdersForTheLastMonthByType(OrderType type) {
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     this.getQuery("for.the.last.month.by.type"))) {
            ps.setString(1, type.name());
            ResultSet rs = ps.executeQuery();
            return this.associateWeeksWithOrderNumbers(rs);
        } catch (SQLException e) {
            throw new CrudException("Could not extract orders numbers", e);
        }
    }

    private EnumMap<WeekOfMonth, Integer> associateWeeksWithOrderNumbers(ResultSet rs)
            throws SQLException {
        EnumMap<WeekOfMonth, Integer> result = new EnumMap<>(WeekOfMonth.class);
        WeekOfMonth weekOfMonth = WeekOfMonth.FIRST_WEEK;
        while (rs.next()) {
            result.put(weekOfMonth, rs.getInt(1));
            LOG.debug("weekOfMonth.next(): {}", weekOfMonth.next());
            weekOfMonth = weekOfMonth.next();
        }
        return result;
    }
}
