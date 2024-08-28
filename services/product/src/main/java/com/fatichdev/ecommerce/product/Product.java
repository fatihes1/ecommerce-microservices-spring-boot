package com.fatichdev.ecommerce.product;

import com.fatichdev.ecommerce.category.Category;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String description;
    private Double availableQuantity;
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
