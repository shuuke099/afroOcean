package com.tinka.products.controller;

import com.tinka.products.dto.ProductRequest;
import com.tinka.products.dto.ProductResponse;
import com.tinka.products.entity.ProductStatus;
import com.tinka.products.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 🔹 Create Product (SELLER ONLY)
    @PostMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest request,
            Principal principal
    ) {
        String sellerId = principal.getName(); // Extracted from JWT
        ProductResponse created = productService.createProduct(request, sellerId);

        // Return 201 Created + Location header
        return ResponseEntity.created(URI.create("/api/products/" + created.getId()))
                .body(created);
    }

    // 🔹 Get Product by ID (Public)
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // 🔹 Get All Products (Public, paginated)
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(productService.getAllProducts(PageRequest.of(page, size)));
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

    // 🔹 Get Products by Seller (Paginated)
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<Page<ProductResponse>> getProductsBySeller(
            @PathVariable String sellerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(productService.getProductsBySeller(sellerId, PageRequest.of(page, size)));
    }

    // 🔹 Search by Title
    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponse>> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(productService.searchProducts(keyword, PageRequest.of(page, size)));
    }

    // 🔹 Filter by Category
    @GetMapping("/category/{category}")
    public ResponseEntity<Page<ProductResponse>> getByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(productService.getProductsByCategory(category, PageRequest.of(page, size)));
    }

    // 🔹 Filter by Country
    @GetMapping("/country/{country}")
    public ResponseEntity<Page<ProductResponse>> getByCountry(
            @PathVariable String country,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(productService.getProductsByCountry(country, PageRequest.of(page, size)));
    }

    // 🔹 Filter by Status
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<ProductResponse>> getByStatus(
            @PathVariable ProductStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(productService.getProductsByStatus(status, PageRequest.of(page, size)));
    }

    // 🔹 Featured Products
    @GetMapping("/featured")
    public ResponseEntity<Page<ProductResponse>> getFeatured(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(productService.getFeaturedProducts(PageRequest.of(page, size)));
    }

    // 🔹 Verified Products
    @GetMapping("/verified")
    public ResponseEntity<Page<ProductResponse>> getVerified(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(productService.getVerifiedProducts(PageRequest.of(page, size)));
    }
}
