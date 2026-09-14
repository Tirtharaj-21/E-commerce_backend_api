package com.ecommerce.shopper.repository;

import com.ecommerce.shopper.entity.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<WishlistItem, Long> {
    long countByUser_Id(Long userId);
    boolean existsByUser_IdAndProductId(Long userId, Long productId);
    void deleteByUser_IdAndProductId(Long userId, Long productId);
}
