package com.dairy.milkcollection.service;

import com.dairy.milkcollection.dto.response.DashboardResponse;
import com.dairy.milkcollection.model.enums.FarmerStatus;
import com.dairy.milkcollection.repository.FarmerRepository;
import com.dairy.milkcollection.repository.MilkCollectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final MilkCollectionRepository milkCollectionRepository;
    private final FarmerRepository farmerRepository;

    public DashboardResponse getDashboardSummary() {
        LocalDate today = LocalDate.now();

        return DashboardResponse.builder()
                .totalMilkToday(milkCollectionRepository.sumMilkLiterByDate(today))
                .totalAmountToday(milkCollectionRepository.sumTotalAmountByDate(today))
                .totalFarmers(farmerRepository.countByStatus(FarmerStatus.ACTIVE))
                .todayEntries(milkCollectionRepository.countByDate(today))
                .averageFat(milkCollectionRepository.avgFatByDate(today))
                .averageSnf(milkCollectionRepository.avgSnfByDate(today))
                .build();
    }
}
