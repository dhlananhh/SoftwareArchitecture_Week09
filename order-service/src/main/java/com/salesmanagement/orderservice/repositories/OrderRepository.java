package com.salesmanagement.orderservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.salesmanagement.orderservice.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	
}
