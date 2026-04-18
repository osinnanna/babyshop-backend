package com.aptechproject.babyshop.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "carts")
public class Cart {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // mappedBy = "cart" means: "Look at the 'cart' field inside the CartItem class to find the database mapping"
    // cascade = CascadeType.ALL means: "If I delete this Cart, delete all the CartItems inside it automatically"
    // fetch = FetchType.EAGER ensures CartItems are loaded immediately with the Cart
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CartItem> items = new ArrayList<>();
}