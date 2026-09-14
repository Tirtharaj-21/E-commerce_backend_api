package com.ecommerce.shopper.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    private Long id; // matches your frontend all_product.js ids — set manually, not generated

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal newPrice;

    @Column(precision = 10, scale = 2)
    private BigDecimal oldPrice;

    @Column(length = 255)
    private String image;

    @Column(length = 50)
    private String category;
}
