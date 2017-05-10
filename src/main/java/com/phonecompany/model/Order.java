package com.phonecompany.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.phonecompany.config.LocalDateTimeDeserializer;
import com.phonecompany.config.LocalDateTimeSerializer;
import com.phonecompany.model.enums.OrderStatus;
import com.phonecompany.model.enums.OrderType;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class Order extends DomainEntity {

    private CustomerServiceDto customerService;
    private CustomerTariff customerTariff;
    @NotNull(message = "Type must not be null")
    private OrderType type;
    @NotNull(message = "Order status must not be null")
    private OrderStatus orderStatus;
    @NotNull(message = "Creation date must not be null")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDate creationDate;
    @NotNull(message = "Execution date must not be null")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDate executionDate;

    public Order() {
    }

    public Order(CustomerServiceDto customerService, CustomerTariff customerTariff,
                 OrderType type, OrderStatus orderStatus, LocalDate creationDate,
                 LocalDate executionDate) {
        this.customerService = customerService;
        this.customerTariff = customerTariff;
        this.type = type;
        this.orderStatus = orderStatus;
        this.creationDate = creationDate;
        this.executionDate = executionDate;
    }

    public Order(CustomerServiceDto customerService,
                 OrderType type, OrderStatus orderStatus, LocalDate creationDate,
                 LocalDate executionDate) {
        this.customerService = customerService;
        this.type = type;
        this.orderStatus = orderStatus;
        this.creationDate = creationDate;
        this.executionDate = executionDate;
    }

    public CustomerServiceDto getCustomerService() {
        return customerService;
    }

    public void setCustomerService(CustomerServiceDto customerService) {
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

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(LocalDate executionDate) {
        this.executionDate = executionDate;
    }

    @Override
    public String toString() {
        return "Order{" +
                ", type=" + type +
                ", orderStatus=" + orderStatus +
                ", creationDate=" + creationDate +
                ", executionDate=" + executionDate +
                '}';
    }
}