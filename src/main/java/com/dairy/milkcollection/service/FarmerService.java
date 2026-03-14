package com.dairy.milkcollection.service;

import com.dairy.milkcollection.dto.request.FarmerCreateRequest;
import com.dairy.milkcollection.dto.request.FarmerUpdateRequest;
import com.dairy.milkcollection.dto.response.FarmerResponse;
import com.dairy.milkcollection.exception.DuplicateResourceException;
import com.dairy.milkcollection.exception.ResourceNotFoundException;
import com.dairy.milkcollection.mapper.FarmerMapper;
import com.dairy.milkcollection.model.Farmer;
import com.dairy.milkcollection.model.enums.FarmerStatus;
import com.dairy.milkcollection.repository.FarmerRepository;
import com.dairy.milkcollection.repository.MilkCollectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FarmerService {

    private final FarmerRepository farmerRepository;
    private final MilkCollectionRepository milkCollectionRepository;
    private final FarmerMapper farmerMapper;

    @Transactional
    public FarmerResponse createFarmer(FarmerCreateRequest request) {
        if (farmerRepository.findByMobile(request.getMobile()).isPresent()) {
            throw new DuplicateResourceException("Mobile number already registered");
        }

        int maxId = farmerRepository.findMaxCustomerIdNumber();
        String customerId = String.valueOf(maxId + 1);

        Farmer farmer = Farmer.builder()
                .customerId(customerId)
                .name(request.getName())
                .mobile(request.getMobile())
                .village(request.getVillage())
                .status(FarmerStatus.ACTIVE)
                .build();

        farmer = farmerRepository.save(farmer);
        return farmerMapper.toResponse(farmer);
    }

    public Page<FarmerResponse> getAllFarmers(String search, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Farmer> farmers;
        if (search != null && !search.isBlank()) {
            farmers = farmerRepository.searchByNameOrCustomerId(search, pageRequest);
        } else {
            farmers = farmerRepository.findAll(pageRequest);
        }

        return farmers.map(farmerMapper::toResponse);
    }

    public FarmerResponse getFarmerById(UUID id) {
        Farmer farmer = farmerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found"));
        return farmerMapper.toResponse(farmer);
    }

    public FarmerResponse getFarmerByCustomerId(String customerId) {
        Farmer farmer = farmerRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found with ID: " + customerId));
        return farmerMapper.toResponse(farmer);
    }

    @Transactional
    public FarmerResponse updateFarmer(UUID id, FarmerUpdateRequest request) {
        Farmer farmer = farmerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found"));

        if (request.getName() != null) farmer.setName(request.getName());
        if (request.getMobile() != null) farmer.setMobile(request.getMobile());
        if (request.getVillage() != null) farmer.setVillage(request.getVillage());
        if (request.getStatus() != null) farmer.setStatus(FarmerStatus.valueOf(request.getStatus()));

        farmer = farmerRepository.save(farmer);
        return farmerMapper.toResponse(farmer);
    }

    @Transactional
    public void deleteFarmer(UUID id) {
        Farmer farmer = farmerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found"));

        if (milkCollectionRepository.existsByFarmerId(id)) {
            throw new IllegalStateException("Cannot delete farmer with existing milk collection records. Deactivate instead.");
        }

        farmerRepository.delete(farmer);
    }

    public List<FarmerResponse> searchFarmers(String query) {
        Page<Farmer> results = farmerRepository.searchByNameOrCustomerId(
                query, PageRequest.of(0, 10));
        return results.getContent().stream().map(farmerMapper::toResponse).toList();
    }
}
