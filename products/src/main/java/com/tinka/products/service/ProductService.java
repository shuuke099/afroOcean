package com.tinka.products.service;

import com.tinka.products.dto.ProductRequest;
import com.tinka.products.dto.ProductResponse;
import com.tinka.products.entity.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    // --- Core CRUD Operations ---
    ProductResponse createProduct(ProductRequest request, String sellerId);
    ProductResponse getProductById(Long id);
    Page<ProductResponse> getAllProducts(Pageable pageable);
    ProductResponse updateProduct(Long id, ProductRequest request, String sellerId);
    void deleteProduct(Long id, String sellerId);

    // --- Seller-Specific ---
    Page<ProductResponse> getProductsBySeller(String sellerId, Pageable pageable);

    // --- Search by keyword ---
    Page<ProductResponse> searchProducts(String keyword, Pageable pageable);

    // --- Filters for marketplace/customer view ---
    Page<ProductResponse> getProductsByCategory(String category, Pageable pageable);
    Page<ProductResponse> getProductsByCountry(String country, Pageable pageable);
    Page<ProductResponse> getProductsByStatus(ProductStatus status, Pageable pageable);
    Page<ProductResponse> getFeaturedProducts(Pageable pageable);
    Page<ProductResponse> getVerifiedProducts(Pageable pageable);
}
