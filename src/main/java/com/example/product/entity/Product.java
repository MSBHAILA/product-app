package com.example.product.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @NotNull
    @Size(max = 30)
    @Column(name = "product_name")
    private String productName;

    @NotNull
    @Size(max = 10)
    @Column(name = "product_group")
    private String productGroup;

}
