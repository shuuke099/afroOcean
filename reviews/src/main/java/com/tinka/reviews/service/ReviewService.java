package com.tinka.reviews.service;

import com.tinka.reviews.dto.ReviewRequest;
import com.tinka.reviews.dto.ReviewResponse;

import java.util.List;

public interface ReviewService {
    ReviewResponse submitReview(ReviewRequest request);
    List<ReviewResponse> getReviewsByProduct(String productId);
    List<ReviewResponse> getReviewsByReviewer(String reviewerId);
    boolean approveReview(Long id);
    boolean rejectReview(Long id);
}
