package com.tinka.products.service;

import com.tinka.products.dto.ProductRequest;
import com.tinka.products.dto.ProductResponse;
import com.tinka.products.entity.Product;
import com.tinka.products.entity.ProductStatus;
import com.tinka.products.exception.ResourceNotFoundException;
import com.tinka.products.kafka.ProductEventPublisher;
import com.tinka.products.mapper.ProductMapper;
import com.tinka.products.repository.ProductRepository;
import com.tinka.products.util.MapperUtil;
import com.tinka.common.events.products.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductEventPublisher eventPublisher;

    @Override
    public ProductResponse createProduct(ProductRequest request, String sellerId) {
        Product product = productMapper.toEntity(request);
        product.setSellerId(sellerId);
        product.setSlug(MapperUtil.toSlug(product.getTitle()));
        product.setStatus(ProductStatus.PENDING);
        product.setVerified(false);
        Product saved = productRepository.save(product);

        // --- Publish ProductCreated event
        eventPublisher.productCreated(ProductCreatedEvent.builder()
                .productId(String.valueOf(saved.getId()))
                .name(saved.getTitle())
                .sellerId(saved.getSellerId())
                .status(saved.getStatus().name())
                .createdAt(saved.getCreatedAt() != null ? saved.getCreatedAt().toInstant(ZoneOffset.UTC) : null)

                .build()
        );

        return productMapper.toResponse(saved);
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = findProductOrThrow(id);
        return productMapper.toResponse(product);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request, String sellerId) {
        Product product = findProductOrThrow(id);

        if (!product.getSellerId().equals(sellerId)) {
            throw new SecurityException("You are not allowed to update this product.");
        }

        product.setTitle(request.getTitle());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setImageUrl(request.getImageUrl());
        product.setImages(request.getImages());
        product.setCategory(request.getCategory());
        product.setBrand(request.getBrand());
        product.setCurrency(request.getCurrency());
        product.setCountry(request.getCountry());
        product.setQuantity(request.getQuantity());
        product.setFeatured(request.isFeatured());
        product.setSlug(MapperUtil.toSlug(request.getTitle()));

        Product updated = productRepository.save(product);

        // --- Publish ProductUpdated event
        eventPublisher.productUpdated(ProductUpdatedEvent.builder()
                .productId(String.valueOf(updated.getId()))
                .name(updated.getTitle())
                .sellerId(updated.getSellerId())
                .status(updated.getStatus().name())
                .updatedAt(updated.getUpdatedAt() != null ? updated.getUpdatedAt().toInstant(ZoneOffset.UTC) : null)
                .build()
        );

        return productMapper.toResponse(updated);
    }

    @Override
    public void deleteProduct(Long id, String sellerId) {
        Product product = findProductOrThrow(id);

        if (!product.getSellerId().equals(sellerId)) {
            throw new SecurityException("You are not allowed to delete this product.");
        }

        productRepository.deleteById(id);

        // --- Publish ProductDeleted event
        eventPublisher.productDeleted(ProductDeletedEvent.builder()
                .productId(String.valueOf(product.getId()))
                .sellerId(product.getSellerId())
                .build()
        );
    }

    @Override
    public List<ProductResponse> getProductsBySeller(String sellerId) {
        return productRepository.findBySellerId(sellerId).stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> searchProducts(String keyword) {
        return productRepository.findByTitleContainingIgnoreCase(keyword).stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getProductsByCategory(String category) {
        return productRepository.findByCategoryIgnoreCaseAndStatus(category, ProductStatus.PUBLISHED).stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getProductsByCountry(String country) {
        return productRepository.findByCountryIgnoreCase(country).stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getProductsByStatus(ProductStatus status) {
        return productRepository.findByStatus(status).stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getFeaturedProducts() {
        return productRepository.findByFeaturedTrueAndStatus(ProductStatus.PUBLISHED).stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getVerifiedProducts() {
        return productRepository.findByVerifiedTrueAndStatus(ProductStatus.PUBLISHED).stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    // 🔐 Internal helper to fetch and validate product existence
    private Product findProductOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }
}
