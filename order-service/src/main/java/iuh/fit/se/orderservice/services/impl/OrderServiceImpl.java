package iuh.fit.se.orderservice.services.impl;

import iuh.fit.se.orderservice.entities.Order;
import iuh.fit.se.orderservice.repositories.OrderRepository;
import iuh.fit.se.orderservice.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import org.springframework.http.ResponseEntity;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, RestTemplate restTemplate, RabbitTemplate rabbitTemplate) {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${rabbitmq.exchange.name}")
    private String orderExchange;

    @Override
    public Order findById(long id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    private boolean checkProductExists(Long productId) {
        String url = "http://product-service/products/" + productId;
        try {
            ResponseEntity<Object> response = restTemplate.getForEntity(url, Object.class);
            return response.getStatusCode().is2xxSuccessful();
        }
        catch (HttpClientErrorException e) {
            return false;
        } catch (Exception e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Order save(Order order) {
        // Check if the product exists before saving the order
        if (!checkProductExists(order.getProductId())) {
            throw new RuntimeException("Product with ID " + order.getProductId() + " not found");
        }
        Order savedOrder = orderRepository.save(order);
        rabbitTemplate.convertAndSend(orderExchange, "order.created", savedOrder.getId());
        return savedOrder;
    }

}
