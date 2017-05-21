package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.CustomerServiceDao;
import com.phonecompany.dao.interfaces.CustomerTariffDao;
import com.phonecompany.dao.interfaces.OrderDao;
import com.phonecompany.dao.interfaces.RowMapper;
import com.phonecompany.exception.dao_layer.CrudException;
import com.phonecompany.exception.dao_layer.EntityInitializationException;
import com.phonecompany.exception.dao_layer.PreparedStatementPopulationException;
import com.phonecompany.model.Order;
import com.phonecompany.model.OrderStatistics;
import com.phonecompany.model.enums.OrderStatus;
import com.phonecompany.model.enums.OrderType;
import com.phonecompany.model.enums.WeekOfMonth;
import com.phonecompany.service.xssfHelper.Statistics;
import com.phonecompany.util.interfaces.QueryLoader;
import com.phonecompany.util.TypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;

import static com.phonecompany.util.TypeMapper.getEnumValueByDatabaseId;
import static com.phonecompany.util.TypeMapper.toLocalDate;
import static com.phonecompany.util.TypeMapper.toSqlDate;

@Repository
public class OrderDaoImpl extends JdbcOperationsImpl<Order>
        implements OrderDao {

    private QueryLoader queryLoader;
    private CustomerServiceDao customerServiceDao;
    private CustomerTariffDao customerTariffDao;

    @Autowired
    public OrderDaoImpl(QueryLoader queryLoader, CustomerServiceDao customerServiceDao,
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
//            order.setCustomerService(DynamicProxy.newInstance(customerServiceId, CUSTOMER_SERVICE_MAPPER));
            order.setCustomerService(customerServiceDao.getById(customerServiceId));
            long customerTariffId = rs.getLong("customer_tariff_id");
//            order.setCustomerTariff(DynamicProxy.newInstance(customerTariffId, CUSTOMER_TARIFF_MAPPER));
            order.setCustomerTariff(customerTariffDao.getById(customerTariffId));
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
    public Order getResumingOrderByCustomerTariffId(Long customerTariffId) {
        return this.executeForObject(this.getQuery("getResumingByCustomerId"), new Object[]{customerTariffId});
    }

    @Override
    public List<Order> getResumingOrderByCustomerServiceId(Long customerId) {
        return this.executeForList(this.getQuery("getResumingServicesByCustomerId"), new Object[]{customerId});
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
        return this.executeForList(query, new Object[]{clientId, size, page * size});
    }

    private Integer getCountByClientId(String query, Long clientId) {
        return this.executeForInt(query, new Object[]{clientId});
    }

    @Override
    public List<Order> getOrdersForCustomerServicesByCustomerIdPaged(Long customerId, int page, int size) {
        return this.executeForList(this.getQuery("getPagedForCustomerServicesByCustomerId"),
                new Object[]{customerId, size, page * size});
    }

    @Override
    public Integer getCountOfServicesByCustomerId(Long customerId) {
        return this.executeForInt(this.getQuery("getCountOfServicesByCustomerId"), new Object[]{customerId});
    }

    @Override
    public List<Order> getTariffOrdersByRegionId(long regionId) {
        return this.executeForList(this.getQuery("tariff.by.region.id"), new Object[]{regionId});
    }

    @Override
    public List<Order> getServiceOrdersByTimePeriod(LocalDate startDate, LocalDate endDate) {
        return this.executeForList(this.getQuery("services.by.time.period"),
                new Object[]{toSqlDate(startDate), toSqlDate(endDate)});
    }

    @Override
    public List<Statistics> getTariffsOrderStatisticsByRegionAndTimePeriod(long regionId,
                                                                           LocalDate startDate,
                                                                           LocalDate endDate) {
        String query = this.getQuery("tariffs.statistics.by.region.and.time.period");
        return this.executeForList(query, new Object[]{regionId, toSqlDate(startDate), toSqlDate(endDate)},
                rs -> new OrderStatistics(rs.getLong("order_count"),
                        rs.getString("tariff_name"),
                        OrderType.valueOf(rs.getString("type")),
                        TypeMapper.toLocalDate(rs.getDate("creation_date"))));
    }

    @Override
    public List<Statistics> getServicesOrderStatisticsByTimePeriod(LocalDate startDate,
                                                                   LocalDate endDate) {
        CallableStatementCreator creator = this
                .getServiceOrdersStatisticsCallableStatementCreator(startDate, endDate);
        RowMapper<Statistics> rowMapper = this.getServiceOrdersStatisticsRowMapper();
        return this.executeForList(creator, rowMapper);
    }

    private CallableStatementCreator getServiceOrdersStatisticsCallableStatementCreator(LocalDate startDate,
                                                                                        LocalDate endDate) {
        String query = this.getQuery("services.statistics.by.time.period");
        return con -> {
            CallableStatement callableStatement = con.prepareCall(query);
            callableStatement.setObject(1, toSqlDate(startDate));
            callableStatement.setObject(2, toSqlDate(endDate));
            return callableStatement;
        };
    }

    private RowMapper<Statistics> getServiceOrdersStatisticsRowMapper() {
        return rs -> new OrderStatistics(rs.getLong("order_count"),
                rs.getString("s_name"),
                OrderType.valueOf(rs.getString("s_type")),
                TypeMapper.toLocalDate(rs.getDate("s_creation_date")));
    }

    @Override
    public EnumMap<WeekOfMonth, Integer> getNumberOfOrdersForTheLastMonthByType(OrderType type) {
        Connection conn = DataSourceUtils.getConnection(getDataSource());
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(
                    this.getQuery("for.the.last.month.by.type"));
            ps.setString(1, type.name());
            ResultSet rs = ps.executeQuery();
            return this.associateWeeksWithOrderNumbers(rs);
        } catch (SQLException e) {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.getDataSource());
            throw new CrudException("Could not extract orders numbers", e);
        } finally {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.getDataSource());
        }
    }

    private EnumMap<WeekOfMonth, Integer> associateWeeksWithOrderNumbers(ResultSet rs)
            throws SQLException {
        EnumMap<WeekOfMonth, Integer> result = new EnumMap<>(WeekOfMonth.class);
        while (rs.next()) {
            long weekNumber = rs.getLong("week_number");
            int numberOfOrders = rs.getInt("number_of_orders");
            WeekOfMonth weekOfMonth = getEnumValueByDatabaseId(WeekOfMonth.class, weekNumber);
            result.put(weekOfMonth, numberOfOrders);
        }
        return result;
    }

    @Override
    public Order getNextResumingOrder() {
        return this.executeForObject(this.getQuery("nextResumingOrder"), new Object[]{});
    }
}
