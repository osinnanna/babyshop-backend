package com.aptechproject.babyshop.model;

import com.aptechproject.babyshop.constant.AppConstants;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/* 
    To make the cart we need to make two things, A `Cart` which every `User` will have and also a `CartItem` which essentially is the Item in the cart of a user. `CartItem`s will reference products (many CartItems can referecne just one product Many to One)
    And also a `Cart` many Cart Items need to beling to one `Cart` belonging to a `User`
*/

@Data
@Entity
@Table(name = "cart_items")
public class CartItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = AppConstants.VALIDATION_QUANTITY_EMPTY)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false) // Ensures every item is tied to a real product
    private Product product;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false) // Ensures every item belongs to a real cart
    private Cart cart;
}
