package com.example.product.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    @NotNull
    @NotEmpty(message = "The name is required.")
    private String name;
    @NotNull
    @NotEmpty(message = "The description is required.")
    private String description;
    @NotNull
    @DecimalMin(value = "0.0" )
    private BigDecimal price;
}
