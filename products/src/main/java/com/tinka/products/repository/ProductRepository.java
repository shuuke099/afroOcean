package com.tinka.products.repository;

import com.tinka.products.entity.Product;
import com.tinka.products.entity.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // 🔍 Basic finders
    List<Product> findBySellerId(String sellerId);
    Optional<Product> findBySlug(String slug);
    List<Product> findByStatus(ProductStatus status);
    List<Product> findByFeaturedTrueAndStatus(ProductStatus status);
    List<Product> findByVerifiedTrueAndStatus(ProductStatus status);

    // 🔍 Filtering
    List<Product> findByCategoryIgnoreCaseAndStatus(String category, ProductStatus status);
    List<Product> findByBrandIgnoreCaseAndQuantityGreaterThan(String brand, int quantity);
    List<Product> findByCountryIgnoreCase(String country);
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // 🔍 Search
    List<Product> findByTitleContainingIgnoreCase(String keyword);

    // 🔍 Sorting
    List<Product> findAllByOrderByPublishedAtDesc();
}
