package com.dairy.milkcollection.dto.request;

import lombok.Data;

@Data
public class FarmerUpdateRequest {
    private String name;
    private String mobile;
    private String village;
    private String status;
}
