package com.dairy.milkcollection.service;

import com.dairy.milkcollection.dto.response.MilkCollectionResponse;
import com.dairy.milkcollection.mapper.MilkCollectionMapper;
import com.dairy.milkcollection.repository.MilkCollectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final MilkCollectionRepository milkCollectionRepository;
    private final MilkCollectionMapper milkCollectionMapper;

    public List<MilkCollectionResponse> getDailyReport(LocalDate date) {
        return milkCollectionRepository.findByDateOrderByCreatedAtDesc(date)
                .stream()
                .map(milkCollectionMapper::toResponse)
                .toList();
    }

    public List<MilkCollectionResponse> getWeeklyReport(LocalDate weekStart) {
        LocalDate weekEnd = weekStart.plusDays(6);
        return milkCollectionRepository.findByDateBetweenOrderByDateDesc(weekStart, weekEnd)
                .stream()
                .map(milkCollectionMapper::toResponse)
                .toList();
    }

    public List<MilkCollectionResponse> getMonthlyReport(int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        return milkCollectionRepository.findByDateBetweenOrderByDateDesc(start, end)
                .stream()
                .map(milkCollectionMapper::toResponse)
                .toList();
    }
}
