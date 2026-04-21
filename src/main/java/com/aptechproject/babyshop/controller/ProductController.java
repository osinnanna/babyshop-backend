package com.aptechproject.babyshop.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.aptechproject.babyshop.constant.AppConstants;
import com.aptechproject.babyshop.dto.RateProductRequest;
import com.aptechproject.babyshop.model.ErrorMessage;
import com.aptechproject.babyshop.model.Product;
import com.aptechproject.babyshop.service.ImageService;
import com.aptechproject.babyshop.service.ProductService;
import com.aptechproject.babyshop.service.RatingService;
import com.aptechproject.babyshop.service.SupabaseStorageService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(AppConstants.API_PRODUCTS)
public class ProductController {

    private final ProductService productService;
    private final ImageService imageService;
    private final SupabaseStorageService supabaseStorageService;
    private final RatingService ratingService;

    public ProductController(ProductService productService, ImageService imageService, SupabaseStorageService supabaseStorageService, RatingService ratingService) {
        this.productService = productService;
        this.imageService = imageService;
        this.supabaseStorageService = supabaseStorageService;
        this.ratingService = ratingService;
    }

    // --- I: Add Product ---
    // 1. Check if an image was uploaded
    // 2. Convert the file into webp using the ImageService
    // 3. Upload the Webp bytes to supabase using the StorageService
    // 4. Receive imageurl after upload and set it to the imageUrl of the inboundProduct
    // 4. Fully save
    // 5. Return a Created status containing the saved product

    @PostMapping(value = AppConstants.API_SERVICE_ADD, consumes = {"multipart/form-data"})
    public ResponseEntity<?> addProduct(@Valid @RequestPart("product") Product inboundProduct,
        @RequestPart(value = "image", required = false) MultipartFile imageFile
    ) {
        try {
            // 1
            if (imageFile != null && !imageFile.isEmpty()) {
                // 2
                byte[] webpBytes = imageService.convertToWebP(imageFile);
                // 3
                String uploadedImageUrl = supabaseStorageService.uploadImage(webpBytes);
                // 4
                inboundProduct.setImageUrl(uploadedImageUrl);
            }

            productService.addProduct(inboundProduct);
            return ResponseEntity.status(HttpStatus.CREATED).body(inboundProduct);
        } catch (IOException | RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(e.getMessage()));
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(e.getMessage()));
        }
    }

    @PostMapping(AppConstants.API_RATE)
    public ResponseEntity<?> rateProduct(@PathVariable Long productId, @Valid @RequestBody RateProductRequest request) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            return ResponseEntity.ok(ratingService.rateProduct(email, productId, request));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(e.getMessage()));
        }
    }

    @GetMapping(AppConstants.API_RATINGS)
    public ResponseEntity<?> getProductRatings(@PathVariable Long productId) {
        try {
            return ResponseEntity.ok(ratingService.getProductRatings(productId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(e.getMessage()));
        }
    }
}