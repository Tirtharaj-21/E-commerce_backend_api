package com.ecommerce.shopper.service;

import com.ecommerce.shopper.dto.*;
import com.ecommerce.shopper.entity.*;
import com.ecommerce.shopper.repository.OrderRepository;
import com.ecommerce.shopper.repository.ProductRepository;
import com.ecommerce.shopper.repository.UserRepository;
import com.ecommerce.shopper.repository.WishlistRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    WishlistRepository wishlistRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    public DashboardResponse buildCustomerDashboard(Long userId) {
        long totalOrders = orderRepository.countByUser_Id(userId);
        long wishlistCount = wishlistRepository.countByUser_Id(userId);

        List<RecentOrderDto> recentOrders = orderRepository
                .findByUser_IdOrderByCreatedAtDesc(userId, PageRequest.of(0, 5))
                .stream()
                .map(o -> new RecentOrderDto(
                        o.getId(),
                        o.getCreatedAt().toLocalDate(),
                        o.getStatus().name(),
                        o.getTotal()))
                .collect(Collectors.toList());

        return new DashboardResponse(totalOrders, wishlistCount, recentOrders);
    }

    @Override
    @Transactional
    public CheckoutResponse checkout(Long userId, CheckoutRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PLACED);

        BigDecimal total = BigDecimal.ZERO;

        for (CheckoutItemRequest itemReq : request.getItems()) {
            if (itemReq.getQuantity() == null || itemReq.getQuantity() <= 0) {
                continue;
            }

            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemReq.getProductId()));

            // Price comes from the DB, never trusted from the client.
            BigDecimal lineTotal = product.getNewPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            total = total.add(lineTotal);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .productId(product.getId())
                    .productName(product.getName())
                    .price(product.getNewPrice())
                    .quantity(itemReq.getQuantity())
                    .build();

            order.getItems().add(orderItem);
        }

        if (order.getItems().isEmpty()) {
            throw new IllegalArgumentException("No valid items to order.");
        }

        order.setTotal(total);
        Order saved = orderRepository.save(order); // cascades to OrderItem rows

        return new CheckoutResponse(saved.getId(), saved.getTotal(), saved.getStatus().name());
    }

    @Override
    @Transactional
    public void toggleWishlist(Long userId, Long productId) {
        boolean exists = wishlistRepository.existsByUser_IdAndProductId(userId, productId);

        if (exists) {
            wishlistRepository.deleteByUser_IdAndProductId(userId, productId);
        } else {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found: " + userId));

            WishlistItem item = WishlistItem.builder()
                    .user(user)
                    .productId(productId)
                    .build();

            wishlistRepository.save(item);
        }
    }
}