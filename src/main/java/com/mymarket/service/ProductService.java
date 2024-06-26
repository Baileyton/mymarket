package com.mymarket.service;

import com.mymarket.dto.ProductResponseDto;
import com.mymarket.entity.Product;
import com.mymarket.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponseDto> getProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }
}