package com.tinka.reviews.repository;

import com.tinka.reviews.entity.Review;
import com.tinka.reviews.entity.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductIdAndStatus(String productId, ReviewStatus status);
    List<Review> findByReviewerId(String reviewerId);
}
