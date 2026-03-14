package com.dairy.milkcollection.service;

import com.dairy.milkcollection.dto.request.MilkCollectionRequest;
import com.dairy.milkcollection.dto.response.MilkCollectionResponse;
import com.dairy.milkcollection.exception.ResourceNotFoundException;
import com.dairy.milkcollection.mapper.MilkCollectionMapper;
import com.dairy.milkcollection.model.Farmer;
import com.dairy.milkcollection.model.MilkCollection;
import com.dairy.milkcollection.repository.FarmerRepository;
import com.dairy.milkcollection.repository.MilkCollectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MilkCollectionService {

    private final MilkCollectionRepository milkCollectionRepository;
    private final FarmerRepository farmerRepository;
    private final MilkCollectionMapper milkCollectionMapper;

    @Transactional
    public MilkCollectionResponse createCollection(MilkCollectionRequest request) {
        Farmer farmer = farmerRepository.findByCustomerId(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Farmer not found with Customer ID: " + request.getCustomerId()));

        BigDecimal rate = calculateRate(request.getDegree());
        BigDecimal totalAmount = calculateTotal(request.getMilkLiter(), rate);

        MilkCollection collection = MilkCollection.builder()
                .farmer(farmer)
                .date(LocalDate.now())
                .milkLiter(request.getMilkLiter())
                .fat(request.getFat())
                .snf(request.getSnf())
                .degree(request.getDegree())
                .rate(rate)
                .totalAmount(totalAmount)
                .build();

        collection = milkCollectionRepository.save(collection);
        return milkCollectionMapper.toResponse(collection);
    }

    public List<MilkCollectionResponse> getTodayCollections() {
        return milkCollectionRepository.findByDateOrderByCreatedAtDesc(LocalDate.now())
                .stream()
                .map(milkCollectionMapper::toResponse)
                .toList();
    }

    public List<MilkCollectionResponse> getByFarmer(UUID farmerId) {
        return milkCollectionRepository.findByFarmerIdOrderByDateDesc(farmerId)
                .stream()
                .map(milkCollectionMapper::toResponse)
                .toList();
    }

    public List<MilkCollectionResponse> getByFarmerAndDateRange(UUID farmerId, LocalDate start, LocalDate end) {
        return milkCollectionRepository.findByFarmerIdAndDateBetweenOrderByDateDesc(farmerId, start, end)
                .stream()
                .map(milkCollectionMapper::toResponse)
                .toList();
    }

    @Transactional
    public MilkCollectionResponse updateCollection(UUID id, MilkCollectionRequest request) {
        MilkCollection collection = milkCollectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Milk collection not found"));

        if (request.getCustomerId() != null) {
            Farmer farmer = farmerRepository.findByCustomerId(request.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Farmer not found"));
            collection.setFarmer(farmer);
        }

        collection.setMilkLiter(request.getMilkLiter());
        collection.setFat(request.getFat());
        collection.setSnf(request.getSnf());
        collection.setDegree(request.getDegree());

        BigDecimal rate = calculateRate(request.getDegree());
        collection.setRate(rate);
        collection.setTotalAmount(calculateTotal(request.getMilkLiter(), rate));

        collection = milkCollectionRepository.save(collection);
        return milkCollectionMapper.toResponse(collection);
    }

    @Transactional
    public void deleteCollection(UUID id) {
        if (!milkCollectionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Milk collection not found");
        }
        milkCollectionRepository.deleteById(id);
    }

    // Rate logic: rate = degree (simple formula as specified)
    private BigDecimal calculateRate(BigDecimal degree) {
        return degree.setScale(2, RoundingMode.HALF_UP);
    }

    // Total = milk_liter * rate
    private BigDecimal calculateTotal(BigDecimal milkLiter, BigDecimal rate) {
        return milkLiter.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }
}
