package com.phonecompany.service;

import com.phonecompany.model.Order;

import java.util.function.Function;
import java.util.function.Predicate;

public abstract class FilteringStrategy {

    public abstract Function<Order, String> getOrderToProductNameMapper();

    public abstract Predicate<Order> getProductNameFilter(String productName);
}
