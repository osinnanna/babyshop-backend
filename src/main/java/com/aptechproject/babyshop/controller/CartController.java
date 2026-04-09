package com.aptechproject.babyshop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptechproject.babyshop.constant.AppConstants;
import com.aptechproject.babyshop.dto.AddToCartRequest;
import com.aptechproject.babyshop.dto.CheckoutReceipt;
import com.aptechproject.babyshop.model.Cart;
import com.aptechproject.babyshop.service.CartService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(AppConstants.API_CART)
public class CartController {
    
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping(value = AppConstants.API_SERVICE_ADD)
    public ResponseEntity<?> addItemToCart(@Valid @RequestBody AddToCartRequest request) {

        try {
            // 1. Get the VIPS email
            // 2. Pass the data to your enterprise logic engine and add productId and quantity
            // 3. Return fully updated cart


            // 1
            String email = SecurityContextHolder.getContext().getAuthentication().getName();

            // 2
            Cart updatedCart = cartService.addItemToCart(email, request.getProductId(), request.getQuantity());

            // 3
            return ResponseEntity.ok(updatedCart);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(AppConstants.API_CHECKOUT)
    public ResponseEntity<?> checkout() {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            CheckoutReceipt receipt = cartService.checkoutCart(email);
            return ResponseEntity.ok(receipt);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getMyCart() {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            return ResponseEntity.ok(cartService.getCart(email));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
