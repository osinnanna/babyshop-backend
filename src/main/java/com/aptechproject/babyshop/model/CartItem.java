package com.aptechproject.babyshop.model;

import com.aptechproject.babyshop.constant.AppConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;

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

    // (Jakson has to return the whole Js object when being returned in the contoller, so after entering the cart reading the fields in `Cart.java` depending on what the data is it sees the list heads inside the first CartItem, reads the above fields, id, quantity and product, gets to cart and the cycle starts again)
    @JsonIgnore // Inf recursion
    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false) // Ensures every item belongs to a real cart
    private Cart cart;
}
