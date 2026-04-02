package com.aptechproject.babyshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aptechproject.babyshop.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{}
