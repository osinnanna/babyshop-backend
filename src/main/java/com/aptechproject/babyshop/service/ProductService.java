package com.aptechproject.babyshop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aptechproject.babyshop.constant.AppConstants;
import com.aptechproject.babyshop.model.Product;
import com.aptechproject.babyshop.repository.ProductRepository;

@Service
public class ProductService {
    
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product addProduct(Product product) {
        productRepository.save(product);
        return product;
    }

    public List<Product> getAllProducts() {
        List<Product> allProducts = productRepository.findAll();
        return allProducts;
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException(AppConstants.ERROR_PRODUCT_INVALID));
    }

    // --- NEW: Update existing product ---
    public Product updateProduct(Long productId, Product updatedData) {
        // 1. Fetch the existing product (this will throw your custom error if not found)
        Product existingProduct = getProductById(productId);

        // 2. Update the fields
        existingProduct.setName(updatedData.getName());
        existingProduct.setCategory(updatedData.getCategory());
        existingProduct.setBrand(updatedData.getBrand());
        existingProduct.setPrice(updatedData.getPrice());
        existingProduct.setStockQuantity(updatedData.getStockQuantity());
        
        // 3. Save and return the updated entity
        return productRepository.save(existingProduct);
    }

    // --- NEW: Delete product ---
    public void deleteProduct(Long productId) {
        // 1. Check if it exists first (throws custom error if not found)
        getProductById(productId);
        
        // 2. Delete from database
        productRepository.deleteById(productId);
    }
}