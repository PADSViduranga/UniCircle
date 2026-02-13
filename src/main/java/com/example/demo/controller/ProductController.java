package com.example.demo.controller;

import com.example.demo.model.Product;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final List<Product> productList = new ArrayList<>(List.of(
            new Product(1, "sachintha", 12),
            new Product(2, "hashini", 16),
            new Product(3, "pathum", 17)
    ));

    @GetMapping
    public List<Product> getAllProducts() {
        return productList;
    }

    @GetMapping("/{productId}") // ✅ fixed name
    public Product getProduct(@PathVariable int productId) {
        return productList.stream()
                .filter(product -> product.getProductId() == productId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        productList.add(product);
        return product;
    }

    @PutMapping("/{productId}")
    public Product updateProduct(@PathVariable int productId, @RequestBody Product updatedProduct) {
        for (Product product : productList) {
            if (product.getProductId() == productId) {
                product.setProductName(updatedProduct.getProductName());
                product.setQuantity(updatedProduct.getQuantity());
                return product;
            }
        }
        throw new RuntimeException("Product not found");
    }
    @DeleteMapping("/{productId}")
        public String deleteProduct(@PathVariable int productId){
        boolean removed=productList.removeIf(product -> product.getProductId()==productId);
        if(!removed){
           throw new RuntimeException("product not found");
        }
        return "product with productId " + productId + " deleted";
    }
}
