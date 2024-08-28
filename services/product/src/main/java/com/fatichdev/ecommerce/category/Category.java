package com.fatichdev.ecommerce.category;

import com.fatichdev.ecommerce.product.Product;
import jakarta.persistence.CascadeType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String description;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private List<Product> products;
}
