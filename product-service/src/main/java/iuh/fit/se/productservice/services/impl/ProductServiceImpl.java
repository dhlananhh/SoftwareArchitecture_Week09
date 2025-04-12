package iuh.fit.se.productservice.services.impl;

import iuh.fit.se.productservice.entities.Product;
import iuh.fit.se.productservice.repositories.ProductRepository;
import iuh.fit.se.productservice.services.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository = null;

	@Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null); // Xử lý trường hợp không tìm thấy
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        Product existingProduct = productRepository.findById(id).orElse(null);
        if (existingProduct != null) {
            existingProduct.setName(product.getName());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setStock(product.getStock());
            return productRepository.save(existingProduct);
        }
        return null; // Xử lý trường hợp không tìm thấy
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
