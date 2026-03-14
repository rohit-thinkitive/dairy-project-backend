package com.dairy.milkcollection.controller;

import com.dairy.milkcollection.dto.request.MilkCollectionRequest;
import com.dairy.milkcollection.dto.response.ApiResponse;
import com.dairy.milkcollection.dto.response.MilkCollectionResponse;
import com.dairy.milkcollection.service.MilkCollectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/milk")
@RequiredArgsConstructor
public class MilkCollectionController {

    private final MilkCollectionService milkCollectionService;

    @PostMapping
    public ResponseEntity<ApiResponse<MilkCollectionResponse>> createCollection(
            @Valid @RequestBody MilkCollectionRequest request) {
        MilkCollectionResponse response = milkCollectionService.createCollection(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Milk collection recorded", response));
    }

    @GetMapping("/today")
    public ResponseEntity<ApiResponse<List<MilkCollectionResponse>>> getTodayCollections() {
        List<MilkCollectionResponse> collections = milkCollectionService.getTodayCollections();
        return ResponseEntity.ok(ApiResponse.success(collections));
    }

    @GetMapping("/by-farmer/{farmerId}")
    public ResponseEntity<ApiResponse<List<MilkCollectionResponse>>> getByFarmer(
            @PathVariable UUID farmerId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        List<MilkCollectionResponse> collections;
        if (from != null && to != null) {
            collections = milkCollectionService.getByFarmerAndDateRange(farmerId, from, to);
        } else {
            collections = milkCollectionService.getByFarmer(farmerId);
        }
        return ResponseEntity.ok(ApiResponse.success(collections));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MilkCollectionResponse>> updateCollection(
            @PathVariable UUID id, @Valid @RequestBody MilkCollectionRequest request) {
        MilkCollectionResponse response = milkCollectionService.updateCollection(id, request);
        return ResponseEntity.ok(ApiResponse.success("Collection updated", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCollection(@PathVariable UUID id) {
        milkCollectionService.deleteCollection(id);
        return ResponseEntity.ok(ApiResponse.success("Collection deleted", null));
    }
}
