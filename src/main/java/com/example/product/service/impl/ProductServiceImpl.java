package com.example.product.service.impl;

import com.example.product.dao.ProductRepository;
import com.example.product.dto.ProductDto;
import com.example.product.entity.Product;
import com.example.product.mapper.ProductMapper;
import com.example.product.service.CacheService;
import com.example.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductMapper productMapper;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CacheService cacheService;

    @Override
    public List<ProductDto> getProducts(long start) {
        List<ProductDto> cachedBatch = cacheService.getCachedProducts(start);
        if (null != cachedBatch) {
            //From the cached data, send only 10 products to the user
            return cachedBatch.stream()
                    .filter(product -> product.getProductId() >= start && product.getProductId() < start + 10)
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public ProductDto saveProduct(ProductDto productDto) {
        if (null != productDto) {
            ProductDto savedProduct = productMapper.productToProductDto(productRepository.save(productMapper.productDtoToProduct(productDto)));
            cacheService.addInCache(savedProduct.getProductId(), savedProduct);
            return savedProduct;
        }
        return null;
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        if (null != productDto && productRepository.existsById(productDto.getProductId())) {
            ProductDto updatedProduct = productMapper.productToProductDto(productRepository.save(productMapper.productDtoToProduct(productDto)));
            cacheService.updateCache(productDto.getProductId(), updatedProduct);
            return updatedProduct;
        }
        return null;
    }

    @Override
    public boolean deleteProducts(long start) {
        List<Product> products = productRepository.deleteProducts(start);
        cacheService.deleteFromCache(start);
        return !CollectionUtils.isEmpty(products);
    }
}
