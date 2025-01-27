package com.example.product.service.impl;

import com.example.product.dao.ProductRepository;
import com.example.product.dto.ProductDto;
import com.example.product.mapper.ProductMapper;
import com.example.product.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CacheServiceImpl implements CacheService {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    ProductRepository productRepository;

    public static final String CACHE_PRODUCTS = "products";
    public static final int LIMIT = 10;
    public static final int BATCH_SIZE = 50;

    private <T> T getFromCache(String cacheName, String key, Class<T> type) {
        return Objects.requireNonNull(cacheManager.getCache(cacheName)).get(key, type);
    }

    private void putInCache(String cacheName, String key, Object value) {
        Objects.requireNonNull(cacheManager.getCache(cacheName)).put(key, value);
    }

    @Override
    @Cacheable(cacheNames = CACHE_PRODUCTS, keyGenerator = "customKeyGenerator")
    public List<ProductDto> getCachedProducts(long start) {
        long startIndex = (start / BATCH_SIZE) * BATCH_SIZE;
        //Fetch 50 products from the database and cache it
        List<ProductDto> products = productMapper.productListToProductDtoList(productRepository.getProducts(startIndex));
        log.info("getProducts | fetch data from db");
        if (!CollectionUtils.isEmpty(products)) {
            return products;
        }
        return null;
    }

    @Override
    public void addInCache(long start, ProductDto productDto) {
        String cacheKey = getCacheKey(start);
        List<ProductDto> cachedBatch = getFromCache(CACHE_PRODUCTS, cacheKey, List.class);
        if (cachedBatch != null) {
            cachedBatch.add(productDto);
            putInCache(CACHE_PRODUCTS, cacheKey, cachedBatch);
        }
    }

    @Override
    public void updateCache(long start, ProductDto updatedProduct) {
        String cacheKey = getCacheKey(start);
        List<ProductDto> cachedBatch = getFromCache(CACHE_PRODUCTS, cacheKey, List.class);
        if (cachedBatch != null) {
            cachedBatch = cachedBatch.stream()
                    .map(p -> Objects.equals(p.getProductId(), updatedProduct.getProductId())
                            ? updatedProduct
                            : p)
                    .collect(Collectors.toList());
            putInCache(CACHE_PRODUCTS, cacheKey, cachedBatch);
        }
    }

    @Override
    public void deleteFromCache(long start) {
        String cacheKey = getCacheKey(start);
        List<ProductDto> cachedBatch = getFromCache(CACHE_PRODUCTS, cacheKey, List.class);
        if (cachedBatch != null) {
            cachedBatch = cachedBatch.stream()
                    .filter(p -> p.getProductId() < start || p.getProductId() > start + LIMIT - 1)
                    .collect(Collectors.toList());
            putInCache(CACHE_PRODUCTS, cacheKey, cachedBatch);
        }
    }

    //Generate cache key for range of products
    private String getCacheKey(long start) {
        long batchStart = ((start - 1) / BATCH_SIZE) * BATCH_SIZE + 1;
        long batchEnd = batchStart + BATCH_SIZE - 1;
        return "products_" + batchStart + "_" + batchEnd;
    }

}