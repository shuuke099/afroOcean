package com.tinka.products.service;

import com.tinka.products.dto.ProductRequest;
import com.tinka.products.dto.ProductResponse;
import com.tinka.products.entity.ProductStatus;


import java.util.List;

public interface ProductService {

    //  Core CRUD Operations
    ProductResponse createProduct(ProductRequest request, String sellerId);
    ProductResponse getProductById(Long id);
    List<ProductResponse> getAllProducts();
    ProductResponse updateProduct(Long id, ProductRequest request, String sellerId);
    void deleteProduct(Long id, String sellerId);

    // Seller-Specific
    List<ProductResponse> getProductsBySeller(String sellerId);

    // Search by title/keyword
    List<ProductResponse> searchProducts(String keyword);

    // Filters for marketplace/customer view
    List<ProductResponse> getProductsByCategory(String category);
    List<ProductResponse> getProductsByCountry(String country);
    List<ProductResponse> getProductsByStatus(ProductStatus status);
    List<ProductResponse> getFeaturedProducts();
    List<ProductResponse> getVerifiedProducts();
}
