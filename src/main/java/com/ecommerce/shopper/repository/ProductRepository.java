package com.ecommerce.shopper.repository;

import com.ecommerce.shopper.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
