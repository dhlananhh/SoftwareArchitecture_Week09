package com.salesmanagement.productservice.kafka;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.salesmanagement.productservice.entities.Product;
import com.salesmanagement.productservice.repositories.ProductRepository;


@Component
public class OrderEventConsumer {

    @Autowired
    private ProductRepository productRepository;

    @KafkaListener(topics = "order-created", groupId = "product-service-group")
    public void handleOrderCreatedEvent(String event) {
        // Parse event (simplified, assume event contains product IDs)
        List<Long> productIds = parseProductIds(event);
        for (Long productId : productIds) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found: " + productId));
            if (product.getStock() > 0) {
                product.setStock(product.getStock() - 1);
                productRepository.save(product);
            } else {
                throw new RuntimeException("Product out of stock: " + productId);
            }
        }
    }

    private List<Long> parseProductIds(String event) {
        // Simplified parsing (in production, use JSON or structured format)
        return List.of(Long.parseLong(event.split(":")[1]));
    }
}