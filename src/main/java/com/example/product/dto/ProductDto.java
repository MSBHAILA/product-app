package com.example.product.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long productId;

    @NotNull
    @Size(max = 30)
    private String productName;

    @NotNull
    @Size(max = 10)
    private String productGroup;

}
