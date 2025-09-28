package com.tinka.products.service;

import com.tinka.products.dto.ProductRequest;
import com.tinka.products.dto.ProductResponse;
import com.tinka.products.entity.Product;
import com.tinka.products.entity.ProductStatus;
import com.tinka.products.exception.ResourceNotFoundException;
import com.tinka.products.kafka.ProductEventPublisher;
import com.tinka.products.kafka.ProductEventMapper;
import com.tinka.products.mapper.ProductMapper;
import com.tinka.products.repository.ProductRepository;
import com.tinka.products.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
        product.setSlug(MapperUtil.toSlug(sellerId + "-" + System.currentTimeMillis()));
        product.setStatus(ProductStatus.PENDING);

        Product saved = productRepository.save(product);
        eventPublisher.productCreated(ProductEventMapper.toCreatedEvent(saved, sellerId));

        return productMapper.toResponse(saved);
    }

    @Override
    public ProductResponse getProductById(Long id) {
        return productMapper.toResponse(findProductOrThrow(id));
    }

    @Override
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::toResponse);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request, String sellerId) {
        Product product = findProductOrThrow(id);

        if (!product.getSellerId().equals(sellerId)) {
            throw new SecurityException("You are not allowed to update this product.");
        }

        productMapper.updateEntity(product, request);
        Product updated = productRepository.save(product);

        eventPublisher.productUpdated(ProductEventMapper.toUpdatedEvent(updated, sellerId));

        if (updated.getStatus() == ProductStatus.PUBLISHED) {
            eventPublisher.productVerified(ProductEventMapper.toVerifiedEvent(updated, sellerId));
        }

        if (updated.getInventory() != null
                && updated.getInventory().getQuantity() != null
                && updated.getInventory().getQuantity() == 0) {
            eventPublisher.productOutOfStock(ProductEventMapper.toOutOfStockEvent(updated, sellerId));
        }

        return productMapper.toResponse(updated);
    }

    @Override
    public void deleteProduct(Long id, String sellerId) {
        Product product = findProductOrThrow(id);

        if (!product.getSellerId().equals(sellerId)) {
            throw new SecurityException("You are not allowed to delete this product.");
        }

        productRepository.delete(product);
        eventPublisher.productDeleted(ProductEventMapper.toDeletedEvent(product, sellerId));
    }

    @Override
    public Page<ProductResponse> getProductsBySeller(String sellerId, Pageable pageable) {
        return productRepository.findBySellerId(sellerId, pageable)
                .map(productMapper::toResponse);
    }

    @Override
    public Page<ProductResponse> searchProducts(String keyword, Pageable pageable) {
        return productRepository.searchByKeyword(keyword, pageable)
                .map(productMapper::toResponse);
    }

    @Override
    public Page<ProductResponse> getProductsByCategory(String category, Pageable pageable) {
        return productRepository.findByCategory(category, ProductStatus.PUBLISHED, pageable)
                .map(productMapper::toResponse);
    }

    @Override
    public Page<ProductResponse> getProductsByCountry(String country, Pageable pageable) {
        return productRepository.findByLocationCountry(country, pageable)
                .map(productMapper::toResponse);
    }

    @Override
    public Page<ProductResponse> getProductsByStatus(ProductStatus status, Pageable pageable) {
        return productRepository.findByStatus(status, pageable)
                .map(productMapper::toResponse);
    }

    @Override
    public Page<ProductResponse> getFeaturedProducts(Pageable pageable) {
        return productRepository.findByMarketing_FeaturedTrueAndStatus(ProductStatus.PUBLISHED, pageable)
                .map(productMapper::toResponse);
    }

    @Override
    public Page<ProductResponse> getVerifiedProducts(Pageable pageable) {
        return productRepository.findByStatus(ProductStatus.PUBLISHED, pageable)
                .map(productMapper::toResponse);
    }

    private Product findProductOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }
}
