package com.example.product.service;

import com.example.product.dto.ProductDto;

import java.util.List;

public interface ProductService {

    List<ProductDto> getProducts(int start);

    ProductDto saveProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);

    boolean deleteProducts(int start);
}
