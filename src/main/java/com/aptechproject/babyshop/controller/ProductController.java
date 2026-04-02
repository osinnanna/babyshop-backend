package com.aptechproject.babyshop.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.aptechproject.babyshop.constant.AppConstants;
import com.aptechproject.babyshop.model.Product;
import com.aptechproject.babyshop.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(AppConstants.API_PRODUCTS)
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // --- I: Add Product ---
    // Make this a POST request to AppConstants.API_ADD_PRODUCT
    // It should accept a @Valid @RequestBody Product
    // Return a Created status containing the saved product

    @PostMapping(AppConstants.API_ADD_PRODUCT)
    public ResponseEntity<?> addProduct(@Valid @RequestBody Product inboundProduct) {

        try {
            productService.addProduct(inboundProduct);
            return ResponseEntity.status(HttpStatus.CREATED).body(inboundProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // --- II: Get All Products ---
    // 1. Get request
    // 2. Return ok with Product List
    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        try {
            List<Product> allProducts = productService.getAllProducts();
            return ResponseEntity.ok(allProducts);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    

}