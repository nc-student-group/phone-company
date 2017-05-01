package com.phonecompany.model;

import com.phonecompany.model.enums.CustomerProductStatus;
import com.phonecompany.model.enums.OrderStatus;
import com.phonecompany.model.enums.OrderType;

import javax.validation.constraints.NotNull;
import java.sql.Date;

public class Order extends DomainEntity {

    private CustomerService customerService;
    private CustomerTariff customerTariff;
    @NotNull(message = "Type must not be null")
    private OrderType type;
    @NotNull(message = "Order status must not be null")
    private OrderStatus orderStatus;
    @NotNull(message = "Creation date must not be null")
    private Date creationDate;
    @NotNull(message = "Execution date must not be null")
    private Date executionDate;

    public Order() {
    }

    public Order(CustomerService customerService, CustomerTariff customerTariff, OrderType type, OrderStatus orderStatus, Date creationDate, Date executionDate) {
        this.customerService = customerService;
        this.customerTariff = customerTariff;
        this.type = type;
        this.orderStatus = orderStatus;
        this.creationDate = creationDate;
        this.executionDate = executionDate;
    }

    public CustomerService getCustomerService() {
        return customerService;
    }

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public CustomerTariff getCustomerTariff() {
        return customerTariff;
    }

    public void setCustomerTariff(CustomerTariff customerTariff) {
        this.customerTariff = customerTariff;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Date executionDate) {
        this.executionDate = executionDate;
    }

    @Override
    public String toString() {
        return "Order{" +
                "customerService=" + customerService +
                ", customerTariff=" + customerTariff +
                ", type=" + type +
                ", orderStatus=" + orderStatus +
                ", creationDate=" + creationDate +
                ", executionDate=" + executionDate +
                '}';
    }
}