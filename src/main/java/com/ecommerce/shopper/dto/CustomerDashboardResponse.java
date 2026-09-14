package com.ecommerce.shopper.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class CustomerDashboardResponse {
    private long totalOrders;
    private long wishlistCount;
    private List<Object> recentOrders;
}
