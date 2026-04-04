package com.aptechproject.babyshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aptechproject.babyshop.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
