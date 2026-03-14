package com.dairy.milkcollection.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class FarmerResponse {
    private String id;
    private String customerId;
    private String name;
    private String mobile;
    private String village;
    private String status;
    private LocalDateTime createdAt;
}
