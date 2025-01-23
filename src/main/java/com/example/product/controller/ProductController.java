package com.example.product.controller;

import com.example.product.constants.ProductApiConstants;
import com.example.product.dto.ProductDto;
import com.example.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ProductApiConstants.API_ENDPOINT_PRODUCT)
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping
    public List<ProductDto> getProducts(@RequestParam(name = "start") int start) {
        List<ProductDto> products = productService.getProducts(start);
        if(null != products){
            return products;
        }
        return null;
    }

    @PostMapping
    public ProductDto saveProduct(@Valid @RequestBody ProductDto productDto) {
        ProductDto savedProduct = productService.saveProduct(productDto);
        if(null != savedProduct){
            return savedProduct;
        }
        return null;
    }

    @PutMapping
    public ProductDto updateProduct(@RequestBody ProductDto productDto) {
        ProductDto updatedProduct = productService.updateProduct(productDto);
        if(null != updatedProduct){
            return updatedProduct;
        }
        return null;
    }

    @DeleteMapping
    public boolean deleteProduct(@RequestParam(name = "start") int start) {
        return productService.deleteProducts(start);
    }
}
