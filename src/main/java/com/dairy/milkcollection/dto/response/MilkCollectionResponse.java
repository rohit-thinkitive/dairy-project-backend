package com.dairy.milkcollection.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MilkCollectionResponse {
    private String id;
    private String farmerId;
    private String customerId;
    private String farmerName;
    private LocalDate date;
    private BigDecimal milkLiter;
    private BigDecimal fat;
    private BigDecimal snf;
    private BigDecimal degree;
    private BigDecimal rate;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
}
