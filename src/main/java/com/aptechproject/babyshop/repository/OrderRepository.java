package com.aptechproject.babyshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aptechproject.babyshop.model.Order;
import com.aptechproject.babyshop.model.User;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByCreatedAtDesc(User user);
}
