package com.dairy.milkcollection.mapper;

import com.dairy.milkcollection.dto.response.FarmerResponse;
import com.dairy.milkcollection.model.Farmer;
import org.springframework.stereotype.Component;

@Component
public class FarmerMapper {

    public FarmerResponse toResponse(Farmer farmer) {
        return FarmerResponse.builder()
                .id(farmer.getId().toString())
                .customerId(farmer.getCustomerId())
                .name(farmer.getName())
                .mobile(farmer.getMobile())
                .village(farmer.getVillage())
                .status(farmer.getStatus().name())
                .createdAt(farmer.getCreatedAt())
                .build();
    }
}
