package iuh.fit.se.productservice.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedListener {

    private static final Logger logger = LoggerFactory.getLogger(OrderCreatedListener.class);

    @RabbitListener(queues = "order.created.queue")
    public void onOrderCreated(String orderId) {
        logger.info("Received order created message. Order ID: {}", orderId);
        // In a real application, you might update product stock or perform other actions here.
    }
}