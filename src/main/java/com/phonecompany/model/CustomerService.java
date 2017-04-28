package com.phonecompany.model;

import com.phonecompany.model.enums.OrderStatus;

import javax.validation.constraints.NotNull;

public class CustomerService extends DomainEntity{

    @NotNull(message = "Customer must not be null")
    private Customer customer;
    @NotNull(message = "Service must not be null")
    private Service service;
    @NotNull(message = "Price must not be null")
    private double price;
    @NotNull(message = "Order status must not be null")
    private OrderStatus orderStatus;

    public CustomerService(){}

    public CustomerService(Customer customer, Service service, double price, OrderStatus orderStatus) {
        this.customer = customer;
        this.service = service;
        this.price = price;
        this.orderStatus = orderStatus;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
