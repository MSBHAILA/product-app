package com.example.product.controller;

import com.example.product.constants.MessageConstants;
import com.example.product.constants.ProductApiConstants;
import com.example.product.dto.ProductDto;
import com.example.product.dto.ResponseDto;
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
    public ResponseDto getProducts(@RequestParam(name = "start") int start) {
        List<ProductDto> products = productService.getProducts(start);
        if (null != products) {
            return new ResponseDto(true, products, MessageConstants.RECORD_RETRIEVED_SUCCESSFULLY);
        }
        return new ResponseDto(false, null, MessageConstants.RECORD_NOT_EXISTS);
    }

    @PostMapping
    public ResponseDto saveProduct(@Valid @RequestBody ProductDto productDto) {
        ProductDto savedProduct = productService.saveProduct(productDto);
        if (null != savedProduct) {
            return new ResponseDto(true, savedProduct, MessageConstants.RECORD_ADDED_SUCCESSFULLY);
        }
        return new ResponseDto(false, null, MessageConstants.RECORD_ALREADY_EXISTS);
    }

    @PutMapping
    public ResponseDto updateProduct(@RequestBody ProductDto productDto) {
        ProductDto updatedProduct = productService.updateProduct(productDto);
        if (null != updatedProduct) {
            return new ResponseDto(true, updatedProduct, MessageConstants.RECORD_ADDED_SUCCESSFULLY);
        }
        return new ResponseDto(false, null, MessageConstants.RECORD_NOT_EXISTS);
    }

    @DeleteMapping
    public ResponseDto deleteProduct(@RequestParam(name = "start") int start) {
        boolean isDeleted = productService.deleteProducts(start);
        if (isDeleted) {
            return new ResponseDto(true, true, MessageConstants.RECORD_DELETED_SUCCESSFULLY);
        }
        return new ResponseDto(false, null, MessageConstants.RECORD_NOT_EXISTS);
    }
}
