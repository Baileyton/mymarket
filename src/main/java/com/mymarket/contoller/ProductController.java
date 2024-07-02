package com.mymarket.contoller;


import com.mymarket.dto.ProductRequestDto;
import com.mymarket.dto.ProductResponseDto;
import com.mymarket.entity.Product;
import com.mymarket.service.ProductService;
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

    @GetMapping("/product/{productId}")
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
}
