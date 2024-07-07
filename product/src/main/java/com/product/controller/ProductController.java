package com.product.controller;

import com.product.dto.ProductRequestDto;
import com.product.dto.ProductResponseDto;
import com.product.entity.Product;
import com.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponseDto>> getProducts() {
        List<ProductResponseDto> products = productService.getProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable("productId") Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/product")
    public ResponseEntity<?> createProduct(@RequestBody ProductRequestDto requestDto) {
        Product createdProduct = productService.createProduct(requestDto);
        return ResponseEntity.ok(createdProduct);
    }


    @GetMapping("/{productId}/quantity")
    public ResponseEntity<Integer> getRemainingQuantity(@PathVariable Long productId) {
        Integer remainingQuantity = productService.getRemainingQuantity(productId);
        return ResponseEntity.ok(remainingQuantity);
    }
}