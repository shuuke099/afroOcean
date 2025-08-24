package com.tinka.products.controller;

import com.tinka.products.dto.ProductRequest;
import com.tinka.products.dto.ProductResponse;
import com.tinka.products.entity.ProductStatus;
import com.tinka.products.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 🔹 Create Product (SELLER ONLY)
    @PostMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request, Principal principal) {
        String sellerId = principal.getName(); // Extracted from JWT
        return ResponseEntity.ok(productService.createProduct(request, sellerId));
    }

    // 🔹 Get Product by ID (Public)
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // 🔹 Get All Products (Public)
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // 🔹 Update Product (SELLER ONLY)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request,
            Principal principal
    ) {
        String sellerId = principal.getName();
        return ResponseEntity.ok(productService.updateProduct(id, request, sellerId));
    }

    // 🔹 Delete Product (SELLER ONLY)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id, Principal principal) {
        String sellerId = principal.getName();
        productService.deleteProduct(id, sellerId);
        return ResponseEntity.noContent().build();
    }

    // 🔹 Get Products by Seller (Public or optional seller portal)
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<ProductResponse>> getProductsBySeller(@PathVariable String sellerId) {
        return ResponseEntity.ok(productService.getProductsBySeller(sellerId));
    }

    // 🔹 Search by Title
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam String keyword) {
        return ResponseEntity.ok(productService.searchProducts(keyword));
    }

    // 🔹 Filter by Category
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponse>> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(productService.getProductsByCategory(category));
    }

    // 🔹 Filter by Country
    @GetMapping("/country/{country}")
    public ResponseEntity<List<ProductResponse>> getByCountry(@PathVariable String country) {
        return ResponseEntity.ok(productService.getProductsByCountry(country));
    }

    // 🔹 Filter by Status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ProductResponse>> getByStatus(@PathVariable ProductStatus status) {
        return ResponseEntity.ok(productService.getProductsByStatus(status));
    }

    // 🔹 Featured Products
    @GetMapping("/featured")
    public ResponseEntity<List<ProductResponse>> getFeatured() {
        return ResponseEntity.ok(productService.getFeaturedProducts());
    }

    // 🔹 Verified Products
    @GetMapping("/verified")
    public ResponseEntity<List<ProductResponse>> getVerified() {
        return ResponseEntity.ok(productService.getVerifiedProducts());
    }
}
