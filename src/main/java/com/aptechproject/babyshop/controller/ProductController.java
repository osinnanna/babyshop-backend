package com.aptechproject.babyshop.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.aptechproject.babyshop.constant.AppConstants;
import com.aptechproject.babyshop.model.Product;
import com.aptechproject.babyshop.service.ImageService;
import com.aptechproject.babyshop.service.ProductService;
import com.aptechproject.babyshop.service.SupabaseStorageService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(AppConstants.API_PRODUCTS)
public class ProductController {

    private final ProductService productService;
    private final ImageService imageService;
    private final SupabaseStorageService supabaseStorageService;

    public ProductController(ProductService productService, ImageService imageService, SupabaseStorageService supabaseStorageService) {
        this.productService = productService;
        this.imageService = imageService;
        this.supabaseStorageService = supabaseStorageService;
    }

    // --- I: Add Product ---
    // 1. Check if an image was uploaded
    // 2. Convert the file into webp using the ImageService
    // 3. Upload the Webp bytes to supabase using the StorageService
    // 4. Receive imageurl after upload and set it to the imageUrl of the inboundProduct
    // 4. Fully save
    // 5. Return a Created status containing the saved product

    @PostMapping(value = AppConstants.API_ADD_PRODUCT, consumes = {"multipart/form-data"})
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