package com.salesmanagement.customerservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.salesmanagement.customerservice.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	
}
