package com.phonecompany.model.proxy;

import com.phonecompany.dao.interfaces.CustomerServiceDao;
import com.phonecompany.dao.interfaces.CustomerTariffDao;
import com.phonecompany.exception.dao_layer.EntityInitializationException;
import com.phonecompany.model.CustomerServiceDto;
import com.phonecompany.model.CustomerTariff;
import com.phonecompany.model.Order;
import com.phonecompany.model.enums.OrderStatus;
import com.phonecompany.model.enums.OrderType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

import static com.phonecompany.util.TypeMapper.toLocalDate;

/**
 * Provides a number of mappers used to map an id corresponding to
 * some entity from the storage to the respective Java object or
 * {@code Collection} of objects
 */
@Component
public class SourceMappers {

    private static CustomerTariffDao customerTariffDao;
    private static CustomerServiceDao customerServiceDao;

    @Autowired
    public SourceMappers(CustomerTariffDao customerTariffDao,
                         CustomerServiceDao customerServiceDao) {
        SourceMappers.customerTariffDao = customerTariffDao;
        SourceMappers.customerServiceDao = customerServiceDao;
    }

    public static final SourceMapper<ResultSet, Order> ORDER_MAPPER =
            new SourceMapper<ResultSet, Order>(Order.class) {
                @Override
                public Function<ResultSet, Order> getMapper() {
                    return rs -> {
                        Order order = new Order();
                        try {
                            order.setId(rs.getLong("id"));
                            order.setType(OrderType.valueOf(rs.getString("type")));
                            long customerServiceId = rs.getLong("customer_service_id");
                            order.setCustomerService(
                                    DynamicProxy.newInstance(customerServiceId, CUSTOMER_SERVICE_MAPPER));
                            order.setOrderStatus(OrderStatus.valueOf(rs.getString("order_status")));
                            long customerTariffId = rs.getLong("customer_tariff_id");
                            order.setCustomerTariff(
                                    DynamicProxy.newInstance(customerTariffId, CUSTOMER_TARIFF_MAPPER));
                            order.setCreationDate(toLocalDate(rs.getDate("creation_date")));
                            order.setExecutionDate(toLocalDate(rs.getDate("execution_date")));
                        } catch (SQLException e) {
                            throw new EntityInitializationException(e);
                        }
                        return order;
                    };
                }
            };

    public static final SourceMapper<Long, CustomerServiceDto> CUSTOMER_SERVICE_MAPPER =
            new SourceMapper<Long, CustomerServiceDto>(CustomerServiceDto.class) {
                @Override
                public Function<Long, CustomerServiceDto> getMapper() {
                    return customerServiceId -> customerServiceDao.getById(customerServiceId);
                }
            };

    public static final SourceMapper<Long, CustomerTariff> CUSTOMER_TARIFF_MAPPER =
            new SourceMapper<Long, CustomerTariff>(CustomerTariff.class) {
                @Override
                public Function<Long, CustomerTariff> getMapper() {
                    return customerTariffId -> customerTariffDao.getById(customerTariffId);
                }
            };
}


