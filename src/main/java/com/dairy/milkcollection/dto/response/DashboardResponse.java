package com.dairy.milkcollection.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DashboardResponse {
    private BigDecimal totalMilkToday;
    private BigDecimal totalAmountToday;
    private long totalFarmers;
    private long todayEntries;
    private BigDecimal averageFat;
    private BigDecimal averageSnf;
}
