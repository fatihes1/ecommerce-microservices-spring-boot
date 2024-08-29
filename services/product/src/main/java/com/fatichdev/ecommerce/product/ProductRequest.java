package com.fatichdev.ecommerce.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequest(
        Integer id,
        @NotNull(message = "Product name is required")
        String name,
        @NotNull(message = "Product description is required")
        String description,
        @Positive(message = "Product available quantity must be greater than 0")
        Double availableQuantity,
        @Positive(message = "Product price must be greater than 0")
        BigDecimal price,
        @NotNull(message = "Product category is required")
        Integer categoryId
) {
}
