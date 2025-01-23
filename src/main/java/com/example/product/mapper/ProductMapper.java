package com.example.product.mapper;

import com.example.product.dto.ProductDto;
import com.example.product.entity.Product;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDto productToProductDto(Product product);

    Product productDtoToProduct(ProductDto productDto);

    @InheritConfiguration(name = "productToProductDto")
    List<ProductDto> productListToProductDtoList(List<Product> productList);
}
