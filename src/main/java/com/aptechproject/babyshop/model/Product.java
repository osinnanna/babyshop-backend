package com.aptechproject.babyshop.model;

import java.math.BigDecimal;

import com.aptechproject.babyshop.constant.AppConstants;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "products")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = AppConstants.VALIDATION_NAME_EMPTY)
    private String name;

    @Column(length = 1000)
    private String description;

    @NotNull(message = AppConstants.VALIDATION_PRICE_EMPTY)
    private BigDecimal price;

    @NotNull(message = AppConstants.VALIDATION_QUANTITY_EMPTY)
    private Integer stockQuantity;
    
    private String imageUrl;
}
