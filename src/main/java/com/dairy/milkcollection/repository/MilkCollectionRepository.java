package com.dairy.milkcollection.repository;

import com.dairy.milkcollection.model.MilkCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface MilkCollectionRepository extends JpaRepository<MilkCollection, UUID> {

    List<MilkCollection> findByDateOrderByCreatedAtDesc(LocalDate date);

    List<MilkCollection> findByFarmerIdOrderByDateDesc(UUID farmerId);

    List<MilkCollection> findByFarmerIdAndDateBetweenOrderByDateDesc(UUID farmerId, LocalDate start, LocalDate end);

    List<MilkCollection> findByDateBetweenOrderByDateDesc(LocalDate start, LocalDate end);

    @Query("SELECT COALESCE(SUM(m.milkLiter), 0) FROM MilkCollection m WHERE m.date = :date")
    java.math.BigDecimal sumMilkLiterByDate(@Param("date") LocalDate date);

    @Query("SELECT COALESCE(SUM(m.totalAmount), 0) FROM MilkCollection m WHERE m.date = :date")
    java.math.BigDecimal sumTotalAmountByDate(@Param("date") LocalDate date);

    @Query("SELECT COALESCE(AVG(m.fat), 0) FROM MilkCollection m WHERE m.date = :date")
    java.math.BigDecimal avgFatByDate(@Param("date") LocalDate date);

    @Query("SELECT COALESCE(AVG(m.snf), 0) FROM MilkCollection m WHERE m.date = :date")
    java.math.BigDecimal avgSnfByDate(@Param("date") LocalDate date);

    long countByDate(LocalDate date);

    boolean existsByFarmerId(UUID farmerId);
}
