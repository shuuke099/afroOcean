package com.tinka.products.repository;

import com.tinka.products.entity.Product;
import com.tinka.products.entity.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // --- Core finders ---
    Page<Product> findBySellerId(String sellerId, Pageable pageable);
    Optional<Product> findBySlug(String slug);
    Page<Product> findByStatus(ProductStatus status, Pageable pageable);

    // --- Marketing filters ---
    Page<Product> findByMarketing_FeaturedTrueAndStatus(ProductStatus status, Pageable pageable);

    // --- Category filter (using translations) ---
    @Query("SELECT p FROM Product p JOIN p.translations t " +
            "WHERE LOWER(t.category) = LOWER(:category) " +
            "AND p.status = :status")
    Page<Product> findByCategory(@Param("category") String category,
                                 @Param("status") ProductStatus status,
                                 Pageable pageable);

    // --- Country filter (using location) ---
    @Query("SELECT p FROM Product p WHERE LOWER(p.location.country) = LOWER(:country)")
    Page<Product> findByLocationCountry(@Param("country") String country, Pageable pageable);

    // --- Keyword search (name + description) ---
    @Query("SELECT p FROM Product p JOIN p.translations t " +
            "WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Product> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
