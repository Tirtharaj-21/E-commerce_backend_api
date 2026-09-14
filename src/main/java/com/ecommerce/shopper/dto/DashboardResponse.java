package com.ecommerce.shopper.dto;
import lombok.Data;

import java.util.List;

@Data
public class DashboardResponse {
    private long totalOrders;
    private long wishlistCount;
    private List<RecentOrderDto> recentOrders;

    public DashboardResponse(long totalOrders, long wishlistCount, List<RecentOrderDto> recentOrders) {
        this.totalOrders = totalOrders;
        this.wishlistCount = wishlistCount;
        this.recentOrders = recentOrders;
    }

    public long getTotalOrders() { return totalOrders; }
    public long getWishlistCount() { return wishlistCount; }
    public List<RecentOrderDto> getRecentOrders() { return recentOrders; }
}
