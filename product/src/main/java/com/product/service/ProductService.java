package com.product.service;

import com.product.dto.ProductRequestDto;
import com.product.dto.ProductResponseDto;
import com.product.entity.Product;
import com.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private static final String PRODUCT_QUANTITY_KEY_PREFIX = "product:quantity:";

    private final ProductRepository productRepository;

    private RedisTemplate<String, Object> redisTemplate;

    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }


    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    public Product createProduct(ProductRequestDto requestDto) {
        Product product = Product.builder()
                .name(requestDto.getName())
                .category(requestDto.getCategory())
                .description(requestDto.getDescription())
                .price(requestDto.getPrice())
                .quantity(requestDto.getQuantity())
                .created_at(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .modified_at(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();

        // Redis에 재고 수량 저장
        redisTemplate.opsForValue().set(PRODUCT_QUANTITY_KEY_PREFIX + product.getId(), product.getQuantity());

        return productRepository.save(product);
    }

    public Integer getProductQuantity(Long productId) {
        Integer quantity = (Integer) redisTemplate.opsForValue().get(PRODUCT_QUANTITY_KEY_PREFIX + productId);
        if (quantity == null) {
            Optional<Product> product = getProductById(productId);
            quantity = product.get().getQuantity();
            redisTemplate.opsForValue().set(PRODUCT_QUANTITY_KEY_PREFIX + productId, quantity);
        }
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. ID: " + productId));
        return product.getQuantity();
    }

    @Transactional
    public void updateProductQuantity(Long productId, Integer quantity) {
        redisTemplate.opsForValue().set(PRODUCT_QUANTITY_KEY_PREFIX + productId, quantity);
        Optional<Product> product = getProductById(productId);
        product.updateQuantity(quantity);
        productRepository.save(product);
    }
}