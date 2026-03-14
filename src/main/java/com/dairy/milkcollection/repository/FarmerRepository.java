package com.dairy.milkcollection.repository;

import com.dairy.milkcollection.model.Farmer;
import com.dairy.milkcollection.model.enums.FarmerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FarmerRepository extends JpaRepository<Farmer, UUID> {

    Optional<Farmer> findByCustomerId(String customerId);

    Optional<Farmer> findByMobile(String mobile);

    List<Farmer> findByStatus(FarmerStatus status);

    @Query("SELECT f FROM Farmer f WHERE " +
           "LOWER(f.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(f.customerId) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Farmer> searchByNameOrCustomerId(@Param("search") String search, Pageable pageable);

    @Query("SELECT COALESCE(MAX(CAST(f.customerId AS int)), 0) FROM Farmer f")
    int findMaxCustomerIdNumber();

    long countByStatus(FarmerStatus status);
}
