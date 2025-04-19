package iuh.fit.se.orderservice.services;

import iuh.fit.se.orderservice.entities.Order;

import java.util.List;

public interface OrderService {
    public Order save(Order order);
    public Order findById(long id);
    List<Order> getAllOrders();
    void deleteOrder(Long id);
}
