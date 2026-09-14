package com.ecommerce.shopper.service;

import com.ecommerce.shopper.dto.CheckoutRequest;
import com.ecommerce.shopper.dto.CheckoutResponse;
import com.ecommerce.shopper.dto.DashboardResponse;

public interface DashboardService {
    DashboardResponse buildCustomerDashboard(Long userId);
    CheckoutResponse checkout(Long userId, CheckoutRequest request);
    void toggleWishlist(Long userId, Long productId);
}
