package com.aptechproject.babyshop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aptechproject.babyshop.model.Product;
import com.aptechproject.babyshop.model.Rating;
import com.aptechproject.babyshop.model.User;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findByUserAndProduct(User user, Product product);
    List<Rating> findByProductOrderByUpdatedAtDesc(Product product);
    long countByProduct(Product product);
}
