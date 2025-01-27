package com.example.product.util;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component("customKeyGenerator")
public class CustomKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        long start = (long) params[0];
        long batchStart = ((start - 1) / 50) * 50 + 1;
        long batchEnd = batchStart + 50 - 1;
        return "products_" + batchStart + "_" + batchEnd;
    }
}