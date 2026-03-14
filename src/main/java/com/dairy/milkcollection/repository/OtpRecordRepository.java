package com.dairy.milkcollection.repository;

import com.dairy.milkcollection.model.OtpRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.Optional;

public interface OtpRecordRepository extends JpaRepository<OtpRecord, Long> {

    Optional<OtpRecord> findTopByMobileAndVerifiedFalseOrderByCreatedAtDesc(String mobile);

    void deleteByExpiresAtBefore(LocalDateTime dateTime);
}
