package com.salesmanagement.orderservice.services.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.kafka.core.KafkaTemplate;

import com.salesmanagement.orderservice.entities.Order;
import com.salesmanagement.orderservice.repositories.OrderRepository;
import com.salesmanagement.orderservice.services.OrderService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public Order createOrder(Order order) {
        // Validate customer exists using Eureka discovery
        webClientBuilder.build()
                .get()
                .uri("http://customer-service/customers/" + order.getCustomerId())
                .retrieve()
                .bodyToMono(Object.class)
                .block();

        // Validate products exist using Eureka discovery
        for (Long productId : order.getProductIds()) {
            webClientBuilder.build()
                    .get()
                    .uri("http://product-service/products SKU /" + productId)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
        }

        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);

        // Publish order created event to Kafka
        for (Long productId : order.getProductIds()) {
            kafkaTemplate.send("order-created", "order:" + productId);
        }

        return savedOrder;
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order updateOrder(Long id, Order order) {
        Optional<Order> existingOrder = orderRepository.findById(id);
        if (existingOrder.isPresent()) {
            Order updatedOrder = existingOrder.get();
            updatedOrder.setCustomerId(order.getCustomerId());
            updatedOrder.setProductIds(order.getProductIds());
            updatedOrder.setTotalAmount(order.getTotalAmount());
            updatedOrder.setStatus(order.getStatus());
            return orderRepository.save(updatedOrder);
        }
        throw new RuntimeException("Order not found");
    }

    @Override
    public void cancelOrder(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            Order updatedOrder = order.get();
            updatedOrder.setStatus("CANCELLED");
            orderRepository.save(updatedOrder);
        } else {
            throw new RuntimeException("Order not found");
        }
    }
}
