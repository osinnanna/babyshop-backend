package com.aptechproject.babyshop.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class RatingResponse {
    private Long ratingId;
    private Long userId;
    private String userName;
    private Integer score;
    private String reviewText;
    private LocalDateTime updatedAt;
}
