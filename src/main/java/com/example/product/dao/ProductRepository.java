package com.example.product.dao;

import com.example.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = """
            SELECT
            	*
            FROM
            	product p
            WHERE
            	p.product_id >= :start
            ORDER BY
            	p.product_id asc
            LIMIT 10""", nativeQuery = true)
    List<Product> getProducts(int start);

    @Query(value = """
            DELETE
            FROM
             product p
            WHERE
            p.product_id >= :start
            AND p.product_id < (:start + 10) returning *""", nativeQuery = true)
    List<Product> deleteProducts(int start);
}
