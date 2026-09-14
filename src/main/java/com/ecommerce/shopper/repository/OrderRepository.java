package com.ecommerce.shopper.repository;

import com.ecommerce.shopper.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    long countByUser_Id(Long userId);
    Page<Order> findByUser_IdOrderByCreatedAtDesc(Long userId, Pageable pageable);}
