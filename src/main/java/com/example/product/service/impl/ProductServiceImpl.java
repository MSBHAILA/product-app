package com.example.product.service.impl;

import com.example.product.dao.ProductRepository;
import com.example.product.dto.ProductDto;
import com.example.product.entity.Product;
import com.example.product.mapper.ProductMapper;
import com.example.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductMapper productMapper;

    @Autowired
    ProductRepository productRepository;

    @Override
    @Cacheable(cacheNames = "products", key = "#start")
    public List<ProductDto> getProducts(int start) {
        List<ProductDto> products = productMapper.productListToProductDtoList(productRepository.getProducts(start));
        log.info("getProducts | fetch data from db");
        if (!CollectionUtils.isEmpty(products)) {
            return products;
        }
        return null;
    }

    @Override
    @CacheEvict(cacheNames = "products", allEntries = true)
    public ProductDto saveProduct(ProductDto productDto) {
        if (null != productDto) {
            return productMapper.productToProductDto(productRepository.save(productMapper.productDtoToProduct(productDto)));
        }
        return null;
    }

    @Override
    @CacheEvict(cacheNames = "products", allEntries = true)
    public ProductDto updateProduct(ProductDto productDto) {
        if (null != productDto && productRepository.existsById(productDto.getProductId())) {
            return productMapper.productToProductDto(productRepository.save(productMapper.productDtoToProduct(productDto)));
        }
        return null;
    }

    @Override
    @CacheEvict(cacheNames = "products", key = "#start")
    public boolean deleteProducts(int start) {
        List<Product> products = productRepository.deleteProducts(start);
        return !CollectionUtils.isEmpty(products);
    }
}
