package com.ecommerce.shopper.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecentOrderDto {
    private Long id;
    private LocalDate date;
    private String status;
    private BigDecimal total;
}
