package com.ecommerce.shopper.controller;

import com.ecommerce.shopper.dto.AdminDashboardResponse;
import com.ecommerce.shopper.dto.UserSummaryResponse;
import com.ecommerce.shopper.entity.User;
import com.ecommerce.shopper.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserRepository userRepository;
    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardResponse> dashboard() {
        AdminDashboardResponse response = AdminDashboardResponse.builder()
                .totalUsers(userRepository.count())
                .totalOrders(0)
                .totalProducts(0)
                .totalRevenue(0)
                .recentOrders(List.of())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserSummaryResponse>> users() {
        List<UserSummaryResponse> response = userRepository.findAll().stream()
                .map(this::toSummary)
                .toList();
        return ResponseEntity.ok(response);
    }

    private UserSummaryResponse toSummary(User user) {
        return UserSummaryResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}
