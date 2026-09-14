package com.ecommerce.shopper.controller;

import com.ecommerce.shopper.dto.CheckoutRequest;
import com.ecommerce.shopper.dto.CheckoutResponse;
import com.ecommerce.shopper.dto.DashboardResponse;
import com.ecommerce.shopper.security.UserPrincipal;
import com.ecommerce.shopper.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
public class DashboardController {
    @Autowired
    DashboardService dashboardService;

    @GetMapping("/dashboard")
    public DashboardResponse getDashboard(@AuthenticationPrincipal UserPrincipal principal) {
        return dashboardService.buildCustomerDashboard(principal.getId());
    }
    @PostMapping("/checkout")
    public CheckoutResponse checkout(@AuthenticationPrincipal UserPrincipal principal,
                                     @RequestBody CheckoutRequest request) {
        return dashboardService.checkout(principal.getId(), request);
    }

    @PostMapping("/wishlist/{productId}")
    public void toggleWishlist(@AuthenticationPrincipal UserPrincipal principal,
                               @PathVariable Long productId) {
        dashboardService.toggleWishlist(principal.getId(), productId);
    }
}
