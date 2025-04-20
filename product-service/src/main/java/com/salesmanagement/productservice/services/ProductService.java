package com.salesmanagement.productservice.services;

import java.util.List;
import java.util.Optional;

import com.salesmanagement.productservice.entities.Product;

public interface ProductService {
	Product createProduct(Product product);
    Optional<Product> getProductById(Long id);
    List<Product> getAllProducts();
    Product updateProduct(Long id, Product product);
    void deleteProduct(Long id);
}
