package com.phonecompany.model;

import com.phonecompany.model.enums.OrderStatus;

import javax.validation.constraints.NotNull;
import java.sql.Date;

public class CustomerTariff extends DomainEntity {
    private Customer customer;
    private Corporate corporate;
    @NotNull(message = "Order date must not be null")
    private Date orderDate;
    @NotNull(message = "Total price must not be null")
    private double totalPrice;
    @NotNull(message = "Order status must not be null")
    private OrderStatus orderStatus;
    @NotNull(message = "Tariff must not be null")
    private Tariff tariff;

    public CustomerTariff(){}

    public CustomerTariff(Customer customer, Corporate corporate, Date orderDate, double totalPrice, OrderStatus orderStatus, Tariff tariff) {
        this.customer = customer;
        this.corporate = corporate;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
        this.tariff = tariff;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Corporate getCorporate() {
        return corporate;
    }

    public void setCorporate(Corporate corporate) {
        this.corporate = corporate;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Tariff getTariff() {
        return tariff;
    }

    public void setTariff(Tariff tariff) {
        this.tariff = tariff;
    }
}
