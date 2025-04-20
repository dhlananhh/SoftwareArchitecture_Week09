package iuh.fit.se.orderservice.services.impl;

import iuh.fit.se.orderservice.entities.Order;
import iuh.fit.se.orderservice.repositories.OrderRepository;
import iuh.fit.se.orderservice.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import org.springframework.http.ResponseEntity;
import java.util.Map;

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

    @Override
    public Order save(Order order) {
        String url = "http://product-service/products/" + order.getProductId();
        ResponseEntity<Map> response;
        try {
            response = restTemplate.getForEntity(url, Map.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ProductNotFoundException("Product with ID " + order.getProductId() + " not found");
            } else {
                throw new RuntimeException("Error retrieving product details: " + e.getMessage());
            }
        }

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Map<String, Object> productDetails = response.getBody();
            String productName = (String) productDetails.get("name"); // Adjust key if necessary
            Double productPrice = (Double) productDetails.get("price"); // Adjust key and type if necessary

            order.setProductName(productName);
            order.setProductPrice(productPrice);
        } else {
            throw new RuntimeException("Invalid response from product-service");
        }

        Order savedOrder = orderRepository.save(order);
        rabbitTemplate.convertAndSend(orderExchange, "order.created", savedOrder.getId());
        return savedOrder;
    }
    
    // Custom exception class
    class ProductNotFoundException extends RuntimeException {
        public ProductNotFoundException(String message) {
            super(message);
        }
    }
}
