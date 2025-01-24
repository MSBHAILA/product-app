package com.example.product.service.impl;

import com.example.product.dao.ProductRepository;
import com.example.product.dto.ProductDto;
import com.example.product.entity.Product;
import com.example.product.mapper.ProductMapper;
import com.example.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductMapper productMapper;

    @Autowired
    ProductRepository productRepository;

    @Override
    public List<ProductDto> getProducts(int start) {
        List<ProductDto> products = productMapper.productListToProductDtoList(productRepository.getProducts(start));
        if (!CollectionUtils.isEmpty(products)) {
            return products;
        }
        return null;
    }

    @Override
    public ProductDto saveProduct(ProductDto productDto) {
        if (null != productDto) {
            return productMapper.productToProductDto(productRepository.save(productMapper.productDtoToProduct(productDto)));
        }
        return null;
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        if (null != productDto && productRepository.existsById(productDto.getProductId())) {
            return productMapper.productToProductDto(productRepository.save(productMapper.productDtoToProduct(productDto)));
        }
        return null;
    }

    @Override
    public boolean deleteProducts(int start) {
        List<Product> products = productRepository.deleteProducts(start);
        return !CollectionUtils.isEmpty(products);
    }
}
