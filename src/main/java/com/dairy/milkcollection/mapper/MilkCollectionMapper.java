package com.dairy.milkcollection.mapper;

import com.dairy.milkcollection.dto.response.MilkCollectionResponse;
import com.dairy.milkcollection.model.MilkCollection;
import org.springframework.stereotype.Component;

@Component
public class MilkCollectionMapper {

    public MilkCollectionResponse toResponse(MilkCollection mc) {
        return MilkCollectionResponse.builder()
                .id(mc.getId().toString())
                .farmerId(mc.getFarmer().getId().toString())
                .customerId(mc.getFarmer().getCustomerId())
                .farmerName(mc.getFarmer().getName())
                .date(mc.getDate())
                .milkLiter(mc.getMilkLiter())
                .fat(mc.getFat())
                .snf(mc.getSnf())
                .degree(mc.getDegree())
                .rate(mc.getRate())
                .totalAmount(mc.getTotalAmount())
                .createdAt(mc.getCreatedAt())
                .build();
    }
}
