package com.aptechproject.babyshop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aptechproject.babyshop.model.Cart;
import com.aptechproject.babyshop.model.User;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
