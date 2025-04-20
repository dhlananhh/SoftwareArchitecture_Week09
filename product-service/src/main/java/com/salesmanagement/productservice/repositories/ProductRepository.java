package com.salesmanagement.productservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.salesmanagement.productservice.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	
}
