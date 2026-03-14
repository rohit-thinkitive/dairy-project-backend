package com.dairy.milkcollection.controller;

import com.dairy.milkcollection.dto.request.FarmerCreateRequest;
import com.dairy.milkcollection.dto.request.FarmerUpdateRequest;
import com.dairy.milkcollection.dto.response.ApiResponse;
import com.dairy.milkcollection.dto.response.FarmerResponse;
import com.dairy.milkcollection.service.FarmerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/farmers")
@RequiredArgsConstructor
public class FarmerController {

    private final FarmerService farmerService;

    @PostMapping
    public ResponseEntity<ApiResponse<FarmerResponse>> createFarmer(@Valid @RequestBody FarmerCreateRequest request) {
        FarmerResponse response = farmerService.createFarmer(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Farmer created", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<FarmerResponse>>> getAllFarmers(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<FarmerResponse> farmers = farmerService.getAllFarmers(search, page, size);
        return ResponseEntity.ok(ApiResponse.success(farmers));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FarmerResponse>> getFarmerById(@PathVariable UUID id) {
        FarmerResponse response = farmerService.getFarmerById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/by-customer-id/{customerId}")
    public ResponseEntity<ApiResponse<FarmerResponse>> getFarmerByCustomerId(@PathVariable String customerId) {
        FarmerResponse response = farmerService.getFarmerByCustomerId(customerId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<FarmerResponse>>> searchFarmers(@RequestParam String q) {
        List<FarmerResponse> results = farmerService.searchFarmers(q);
        return ResponseEntity.ok(ApiResponse.success(results));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FarmerResponse>> updateFarmer(
            @PathVariable UUID id, @RequestBody FarmerUpdateRequest request) {
        FarmerResponse response = farmerService.updateFarmer(id, request);
        return ResponseEntity.ok(ApiResponse.success("Farmer updated", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFarmer(@PathVariable UUID id) {
        farmerService.deleteFarmer(id);
        return ResponseEntity.ok(ApiResponse.success("Farmer deleted", null));
    }
}
