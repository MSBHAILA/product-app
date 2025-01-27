package com.example.product.service;

import com.example.product.dto.ProductDto;

import java.util.List;

public interface CacheService {

    void addInCache(long start, ProductDto productDto);

    void updateCache(long start, ProductDto updatedProduct);

    void deleteFromCache(long start);

    List<ProductDto> getCachedProducts(long start);
}