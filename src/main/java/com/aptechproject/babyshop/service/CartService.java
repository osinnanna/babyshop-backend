package com.aptechproject.babyshop.service;

import java.math.BigDecimal;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aptechproject.babyshop.constant.AppConstants;
import com.aptechproject.babyshop.dto.CheckoutReceipt;
import com.aptechproject.babyshop.model.Cart;
import com.aptechproject.babyshop.model.CartItem;
import com.aptechproject.babyshop.model.Order;
import com.aptechproject.babyshop.model.OrderItem;
import com.aptechproject.babyshop.model.Product;
import com.aptechproject.babyshop.model.User;
import com.aptechproject.babyshop.repository.CartItemRepository;
import com.aptechproject.babyshop.repository.CartRepository;
import com.aptechproject.babyshop.repository.OrderRepository;
import com.aptechproject.babyshop.repository.ProductRepository;
import com.aptechproject.babyshop.repository.UserRepository;

@Service
public class CartService {
    
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, UserRepository userRepository, ProductRepository productRepository, OrderRepository orderRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public Cart addItemToCart(String userEmail, Long productId, Integer quantity) {
        // STEP 1: Find the User using the userRepository and the userEmail. 
        // (Throw an exception if not found)
        
        // STEP 2: Find the User's Cart using your new cartRepository.findByUser() method.
        // (Throw an exception if not found)
        
        // STEP 3: Find the Product using productRepository.findById(productId).
        // (Throw an exception if not found)
        
        // STEP 4: Create a new CartItem object.
        // - Set the quantity
        // - Set the product
        // - Set the cart
        
        // STEP 5: Save the new CartItem using cartItemRepository.save()
        
        // STEP 6: Return the Cart so the controller can send it back to the Flutter app!

        // 1
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException(AppConstants.ERROR_INVALID_CREDENTIALS));
        
        // 2
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new RuntimeException(AppConstants.ERROR_CART_INVALID));

        // 3
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException(AppConstants.ERROR_PRODUCT_INVALID));

        // Check if stock is enough for request (avoid negative stock)
        if (quantity > product.getStockQuantity()) {
            throw new RuntimeException(AppConstants.ERROR_STOCK_OVERDRAFT);
        }

        // 4
        CartItem item = new CartItem();
        item.setQuantity(quantity);
        item.setProduct(product);
        item.setCart(cart);

        // 5 - To database
        cartItemRepository.save(item);
        // And in memory
        cart.getItems().add(item);

        // 6
        return cart;
    }

    @Transactional
    public CheckoutReceipt checkoutCart(String userEmail) {
        // 1. Find user and their cart
        // 2. Check if the cart is empty. If it is, throw an exception ("Cart is empty!")
        // 3. Loop through the items in the Cart (cart.item) -> add to totalAmount and add to quantity -> grab the product and reduce from its stockQuantity

        // 1
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException(AppConstants.ERROR_INVALID_CREDENTIALS));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new RuntimeException(AppConstants.ERROR_CART_INVALID));

        if (cart.getItems().isEmpty()) throw new RuntimeException(AppConstants.ERROR_CART_EMPTY);

        BigDecimal totalAmount = BigDecimal.ZERO;
        int totalItems = 0;

        Order order = new Order();
        order.setUser(user);

        // 3
        for (CartItem item : cart.getItems()) {
            Product productAttachedToItem = item.getProduct();

            BigDecimal itemTotal = productAttachedToItem.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
            totalItems += item.getQuantity();

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductId(productAttachedToItem.getId());
            orderItem.setProductName(productAttachedToItem.getName());
            orderItem.setProductImageUrl(productAttachedToItem.getImageUrl());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setUnitPrice(productAttachedToItem.getPrice());
            orderItem.setLineTotal(itemTotal);
            order.getItems().add(orderItem);

            productAttachedToItem.setStockQuantity(productAttachedToItem.getStockQuantity() - item.getQuantity());
            productRepository.save(productAttachedToItem);
        }

        order.setTotalAmount(totalAmount);
        order.setTotalItems(totalItems);
        orderRepository.save(order);

        // 4. Empty the cart in the database!
        cartItemRepository.deleteAll(cart.getItems());

        // 5. Empty the cart in Java memory!
        cart.getItems().clear();

        // 6. make the receipt
        CheckoutReceipt receipt = new CheckoutReceipt();
        receipt.setMessage(AppConstants.VALIDATION_CHECKOUT_SUCCESSFUL);
        receipt.setTotalItemsBought(totalItems);
        receipt.setTotalAmountPaid(totalAmount.doubleValue());

        return receipt;
    }

    @Transactional(readOnly = true)
    public Cart getCart(String userEmail) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException(AppConstants.ERROR_INVALID_CREDENTIALS));
        return cartRepository.findByUser(user).orElseThrow(() -> new RuntimeException(AppConstants.ERROR_CART_INVALID));
    }
}
