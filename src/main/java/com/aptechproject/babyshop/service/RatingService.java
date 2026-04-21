package com.aptechproject.babyshop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aptechproject.babyshop.constant.AppConstants;
import com.aptechproject.babyshop.dto.ProductRatingSummaryResponse;
import com.aptechproject.babyshop.dto.RateProductRequest;
import com.aptechproject.babyshop.dto.RatingResponse;
import com.aptechproject.babyshop.model.Product;
import com.aptechproject.babyshop.model.Rating;
import com.aptechproject.babyshop.model.User;
import com.aptechproject.babyshop.repository.ProductRepository;
import com.aptechproject.babyshop.repository.RatingRepository;
import com.aptechproject.babyshop.repository.UserRepository;

@Service
public class RatingService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final RatingRepository ratingRepository;

    public RatingService(UserRepository userRepository, ProductRepository productRepository, RatingRepository ratingRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.ratingRepository = ratingRepository;
    }

    @Transactional
    public ProductRatingSummaryResponse rateProduct(String userEmail, Long productId, RateProductRequest request) {
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException(AppConstants.ERROR_INVALID_CREDENTIALS));

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException(AppConstants.ERROR_PRODUCT_INVALID));

        validateRequest(request);

        Rating rating = ratingRepository.findByUserAndProduct(user, product).orElseGet(Rating::new);
        rating.setUser(user);
        rating.setProduct(product);
        rating.setScore(request.getScore());
        rating.setReviewText(sanitizeReview(request.getReviewText()));
        ratingRepository.save(rating);

        updateProductRatingAggregates(product);

        return mapToSummary(product, ratingRepository.findByProductOrderByUpdatedAtDesc(product));
    }

    @Transactional(readOnly = true)
    public ProductRatingSummaryResponse getProductRatings(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException(AppConstants.ERROR_PRODUCT_INVALID));

        return mapToSummary(product, ratingRepository.findByProductOrderByUpdatedAtDesc(product));
    }

    private void validateRequest(RateProductRequest request) {
        if (request.getScore() == null || request.getScore() < 1 || request.getScore() > 5) {
            throw new RuntimeException(AppConstants.ERROR_RATING_INVALID);
        }

        if (request.getReviewText() != null && request.getReviewText().length() > 500) {
            throw new RuntimeException(AppConstants.ERROR_REVIEW_TOO_LONG);
        }
    }

    private String sanitizeReview(String reviewText) {
        if (reviewText == null) {
            return null;
        }

        String trimmed = reviewText.trim();
        if (trimmed.isEmpty()) {
            return null;
        }

        return trimmed;
    }

    private void updateProductRatingAggregates(Product product) {
        List<Rating> allRatings = ratingRepository.findByProductOrderByUpdatedAtDesc(product);
        double average = allRatings.stream().mapToInt(Rating::getScore).average().orElse(0.0);

        product.setAverageRating(Math.round(average * 10.0) / 10.0);
        product.setRatingCount(allRatings.size());
        productRepository.save(product);
    }

    private ProductRatingSummaryResponse mapToSummary(Product product, List<Rating> ratings) {
        ProductRatingSummaryResponse response = new ProductRatingSummaryResponse();
        response.setProductId(product.getId());
        response.setAverageRating(product.getAverageRating());
        response.setRatingCount(product.getRatingCount());
        response.setRatings(ratings.stream().map(this::mapRating).toList());
        return response;
    }

    private RatingResponse mapRating(Rating rating) {
        RatingResponse response = new RatingResponse();
        response.setRatingId(rating.getId());
        response.setUserId(rating.getUser().getId());
        response.setUserName(rating.getUser().getName());
        response.setScore(rating.getScore());
        response.setReviewText(rating.getReviewText());
        response.setUpdatedAt(rating.getUpdatedAt());
        return response;
    }
}
