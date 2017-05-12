package com.phonecompany.model;

import com.phonecompany.model.enums.CustomerProductStatus;

import javax.validation.constraints.NotNull;

public class CustomerServiceDto extends DomainEntity {

    @NotNull(message = "Customer must not be null")
    private Customer customer;
    @NotNull(message = "Service must not be null")
    private Service service;
    @NotNull(message = "Price must not be null")
    private double price;
    @NotNull(message = "Order status must not be null")
    private CustomerProductStatus customerProductStatus;

    public CustomerServiceDto() {
    }

    public CustomerServiceDto(Customer customer, Service service,
                              double price, CustomerProductStatus customerProductStatus) {
        this.customer = customer;
        this.service = service;
        this.price = price;
        this.customerProductStatus = customerProductStatus;
    }

    public CustomerServiceDto(Long id, Customer customer, Service service,
                              double price, CustomerProductStatus customerProductStatus) {
        super(id);
        this.customer = customer;
        this.service = service;
        this.price = price;
        this.customerProductStatus = customerProductStatus;
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

    public CustomerProductStatus getCustomerProductStatus() {
        return customerProductStatus;
    }

    public void setCustomerProductStatus(CustomerProductStatus customerProductStatus) {
        this.customerProductStatus = customerProductStatus;
    }

    @Override
    public String toString() {
        return "CustomerServiceDto{" +
                "customer=" + customer +
                ", service=" + service +
                ", price=" + price +
                ", customerProductStatus=" + customerProductStatus +
                '}';
    }
}
