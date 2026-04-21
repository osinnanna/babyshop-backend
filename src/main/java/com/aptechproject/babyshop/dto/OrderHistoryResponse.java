package com.aptechproject.babyshop.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class OrderHistoryResponse {
    private Long orderId;
    private LocalDateTime createdAt;
    private Integer totalItems;
    private BigDecimal totalAmount;
    private List<OrderHistoryItemResponse> items;
}
