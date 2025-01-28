package com.example.product.service.impl;

import com.example.product.dao.ProductRepository;
import com.example.product.dto.ProductDto;
import com.example.product.mapper.ProductMapper;
import com.example.product.service.CacheService;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CacheServiceImpl implements CacheService {

    @Autowired
    HazelcastInstance hazelcastInstance;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    ProductRepository productRepository;

    public static final String CACHE_PRODUCTS = "products";
    public static final int LIMIT = 10;
    public static final int BATCH_SIZE = 50;

    private <K, V> IMap<K, V> getCache(String cacheName) {
        return hazelcastInstance.getMap(cacheName);
    }

    @Override
    public List<ProductDto> getCachedProducts(long start) {
        long end = start + LIMIT - 1;

        // Determine if there are overlapping batches
        long batchStart1 = ((start - 1) / BATCH_SIZE) * BATCH_SIZE + 1;
        long batchStart2 = ((end - 1) / BATCH_SIZE) * BATCH_SIZE + 1;

        // Fetch data from the cache or database for both batches
        List<ProductDto> products = new ArrayList<>();
        products.addAll(getProductsFromCacheOrDb(batchStart1));
        if (batchStart2 != batchStart1) {
            products.addAll(getProductsFromCacheOrDb(batchStart2));
        }
        return products;

    }

    private List<ProductDto> getProductsFromCacheOrDb(long start) {
        String cacheKey = getCacheKey(start);
        IMap<String, List<ProductDto>> productCache = getCache(CACHE_PRODUCTS);
        List<ProductDto> cachedProducts = productCache.get(cacheKey);
        //If data in cache is present, return it.
        if (!CollectionUtils.isEmpty(cachedProducts)) {
            return cachedProducts;
        }
        //Fetch 50 products from the database and cache it.
        long startIndex = (start / BATCH_SIZE) * BATCH_SIZE;
        List<ProductDto> products = productMapper.productListToProductDtoList(productRepository.getProducts(startIndex));
        log.info("getProducts | fetch data from db");
        if (!CollectionUtils.isEmpty(products)) {
            productCache.put(cacheKey, products);
            return products;
        }
        return Collections.emptyList();
    }

    @Override
    public void addInCache(long start, ProductDto productDto) {
        String cacheKey = getCacheKey(start);
        IMap<String, List<ProductDto>> productCache = getCache(CACHE_PRODUCTS);
        List<ProductDto> cachedProducts = productCache.get(cacheKey);
        if (cachedProducts != null) {
            cachedProducts.add(productDto);
            productCache.put(cacheKey, cachedProducts);
        }
    }

    @Override
    public void updateCache(long start, ProductDto updatedProduct) {
        String cacheKey = getCacheKey(start);
        IMap<String, List<ProductDto>> productCache = getCache(CACHE_PRODUCTS);
        List<ProductDto> cachedProducts = productCache.get(cacheKey);
        if (!CollectionUtils.isEmpty(cachedProducts)) {
            cachedProducts = cachedProducts.stream()
                    .map(p -> Objects.equals(p.getProductId(), updatedProduct.getProductId())
                            ? updatedProduct
                            : p)
                    .toList();
            productCache.put(cacheKey, cachedProducts);
        }
    }

    @Override
    public void deleteFromCache(long start) {
        String cacheKey = getCacheKey(start);
        IMap<String, List<ProductDto>> productCache = getCache(CACHE_PRODUCTS);
        List<ProductDto> cachedProducts = productCache.get(cacheKey);
        if (!CollectionUtils.isEmpty(cachedProducts)) {
            cachedProducts = cachedProducts.stream()
                    .filter(p -> p.getProductId() < start || p.getProductId() > start + LIMIT - 1)
                    .collect(Collectors.toList());
            productCache.put(cacheKey, cachedProducts);
        }
    }

    //Generate cache key for range of products
    private String getCacheKey(long start) {
        long batchStart = ((start - 1) / BATCH_SIZE) * BATCH_SIZE + 1;
        long batchEnd = batchStart + BATCH_SIZE - 1;
        return "products_" + batchStart + "_" + batchEnd;
    }

}