package iuh.fit.se.productservice.controllers;

import iuh.fit.se.productservice.entities.Product;
import iuh.fit.se.productservice.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Đánh dấu đây là một REST controller
@RequestMapping("/products") // Map các request tới "/products" tới controller này
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping // Map GET requests tới "/products"
	public ResponseEntity<List<Product>> getAllProducts() {
	    List<Product> products = productService.getAllProducts();
	    return new ResponseEntity<>(products, HttpStatus.OK);
	}

	@GetMapping("/{id}") // Map GET requests tới "/products/{id}"
	public ResponseEntity<Product> getProductById(@PathVariable Long id) {
	    Product product = productService.getProductById(id);
	    if (product != null) {
	        return new ResponseEntity<>(product, HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	}

	@PostMapping // Map POST requests tới "/products"
	public ResponseEntity<Product> createProduct(@RequestBody Product product) {
	    Product createdProduct = productService.createProduct(product);
	    return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
	}

	@PutMapping("/{id}") // Map PUT requests tới "/products/{id}"
	public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
	    Product updatedProduct = productService.updateProduct(id, product);
	    if (updatedProduct != null) {
	        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	}

	@DeleteMapping("/{id}") // Map DELETE requests tới "/products/{id}"
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
