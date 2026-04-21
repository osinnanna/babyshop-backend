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
}
