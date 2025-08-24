package com.tinka.reviews.service;

import com.tinka.common.events.reviews.ReviewCreatedEvent;
import com.tinka.reviews.dto.ReviewRequest;
import com.tinka.reviews.dto.ReviewResponse;
import com.tinka.reviews.entity.Review;
import com.tinka.reviews.entity.ReviewStatus;
import com.tinka.reviews.kafka.ReviewEventPublisher;
import com.tinka.reviews.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewEventPublisher eventPublisher; // ✅ add publisher

    @Override
    public ReviewResponse submitReview(ReviewRequest request) {
        // 1) Persist as PENDING (or APPROVED if you auto-approve)
        Review review = Review.builder()
                .productId(request.getProductId())
                .reviewerId(request.getReviewerId())
                .reviewerName(request.getReviewerName())
                .rating(request.getRating())
                .comment(request.getComment())
                .status(ReviewStatus.PENDING)
                .build();

        Review saved = reviewRepository.save(review);

        // 2) Publish ReviewCreatedEvent
        eventPublisher.reviewCreated(
                ReviewCreatedEvent.builder()
                        .reviewId(saved.getId().toString())
                        .productId(saved.getProductId())
                        .userId(saved.getReviewerId())     // reviewer
                        .rating(saved.getRating())
                        .comment(saved.getComment())
                        .createdAt(toInstant(saved.getCreatedAt()))
                        .source("reviews")
                        .build()
        );

        // 3) Return DTO
        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByProduct(String productId) {
        return reviewRepository.findByProductIdAndStatus(productId, ReviewStatus.APPROVED)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByReviewer(String reviewerId) {
        return reviewRepository.findByReviewerId(reviewerId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public boolean approveReview(Long id) {
        return updateReviewStatus(id, ReviewStatus.APPROVED);
    }

    @Override
    public boolean rejectReview(Long id) {
        return updateReviewStatus(id, ReviewStatus.REJECTED);
    }

    private boolean updateReviewStatus(Long id, ReviewStatus status) {
        return reviewRepository.findById(id).map(review -> {
            review.setStatus(status);
            reviewRepository.save(review);
            return true;
        }).orElse(false);
    }

    // ---- helpers ----
    private ReviewResponse toDto(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .productId(review.getProductId())
                .reviewerId(review.getReviewerId())
                .reviewerName(review.getReviewerName())
                .rating(review.getRating())
                .comment(review.getComment())
                .status(review.getStatus())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }

    private static java.time.Instant toInstant(java.time.LocalDateTime ldt) {
        return ldt == null ? null : ldt.atZone(ZoneId.systemDefault()).toInstant();
    }
}
