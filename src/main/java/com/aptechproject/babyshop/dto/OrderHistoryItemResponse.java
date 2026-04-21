package com.aptechproject.babyshop.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderHistoryItemResponse {
    private Long productId;
    private String productName;
    private String productImageUrl;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;
}
