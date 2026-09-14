package com.ecommerce.shopper.service;

import com.ecommerce.shopper.repository.InvalidatedTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Housekeeping: rows in invalidated_tokens are only needed until the token's
 * own expiry passes, otherwise the table grows forever.
 */
@Service
@RequiredArgsConstructor
public class TokenCleanupService {

    private final InvalidatedTokenRepository invalidatedTokenRepository;

    @Transactional
    @Scheduled(fixedRate = 60 * 60 * 1000) // every hour
    public void purgeExpiredTokens() {
        invalidatedTokenRepository.deleteAllByExpiryDateBefore(LocalDateTime.now());
    }
}
