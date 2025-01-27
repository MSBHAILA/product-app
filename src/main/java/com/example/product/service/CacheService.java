package com.example.product.service;

import com.example.product.dto.ProductDto;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface CacheService {

    void addInCache(long start, ProductDto productDto);

    void updateCache(long start, ProductDto updatedProduct);

    void deleteFromCache(long start);

    @Cacheable(cacheNames = "products", keyGenerator = "customKeyGenerator")
    List<ProductDto> getCachedProducts(long start);
}