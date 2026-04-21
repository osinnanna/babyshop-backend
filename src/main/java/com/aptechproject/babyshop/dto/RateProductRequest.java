package com.aptechproject.babyshop.dto;

import com.aptechproject.babyshop.constant.AppConstants;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RateProductRequest {

    @Min(value = 1, message = AppConstants.ERROR_RATING_INVALID)
    @Max(value = 5, message = AppConstants.ERROR_RATING_INVALID)
    private Integer score;

    @Size(max = 500, message = AppConstants.ERROR_REVIEW_TOO_LONG)
    private String reviewText;
}
