package com.salesmanagement.orderservice.services;


import java.util.List;
import java.util.Optional;

import com.salesmanagement.orderservice.entities.Order;


public interface OrderService {
    Order createOrder(Order order);
    Optional<Order> getOrderById(Long id);
    List<Order> getAllOrders();
    Order updateOrder(Long id, Order order);
    void cancelOrder(Long id);
}