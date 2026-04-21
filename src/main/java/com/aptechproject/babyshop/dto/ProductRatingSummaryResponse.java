package com.aptechproject.babyshop.dto;

import java.util.List;

import lombok.Data;

@Data
public class ProductRatingSummaryResponse {
    private Long productId;
    private Double averageRating;
    private Integer ratingCount;
    private List<RatingResponse> ratings;
}
