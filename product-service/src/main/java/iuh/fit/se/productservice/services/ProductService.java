package iuh.fit.se.productservice.services;

import iuh.fit.se.productservice.entities.Product;

import java.util.List;

public interface ProductService {
	List<Product> getAllProducts();
    Product getProductById(Long id);
    Product createProduct(Product product);
    Product updateProduct(Long id, Product product);
    void deleteProduct(Long id);
}
