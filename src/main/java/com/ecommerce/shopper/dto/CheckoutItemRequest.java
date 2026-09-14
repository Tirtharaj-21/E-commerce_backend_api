package com.ecommerce.shopper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutItemRequest {
    private Long productId;
    private Integer quantity;
}
