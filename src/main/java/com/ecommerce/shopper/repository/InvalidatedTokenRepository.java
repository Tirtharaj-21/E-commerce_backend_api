package com.ecommerce.shopper.repository;

import com.ecommerce.shopper.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, Long> {

    boolean existsByToken(String token);

    void deleteAllByExpiryDateBefore(LocalDateTime dateTime);
}
