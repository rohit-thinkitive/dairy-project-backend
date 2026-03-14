package com.dairy.milkcollection.service;

import com.dairy.milkcollection.exception.InvalidOtpException;
import com.dairy.milkcollection.exception.ResourceNotFoundException;
import com.dairy.milkcollection.model.OtpRecord;
import com.dairy.milkcollection.repository.FarmerRepository;
import com.dairy.milkcollection.repository.OtpRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpService {

    private final OtpRecordRepository otpRecordRepository;
    private final FarmerRepository farmerRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${app.otp.expiry-minutes}")
    private int otpExpiryMinutes;

    @Transactional
    public String generateAndSendOtp(String mobile) {
        farmerRepository.findByMobile(mobile)
                .orElseThrow(() -> new ResourceNotFoundException("No farmer registered with mobile: " + mobile));

        String otp = String.format("%06d", secureRandom.nextInt(1000000));

        OtpRecord record = OtpRecord.builder()
                .mobile(mobile)
                .otpCode(otp)
                .expiresAt(LocalDateTime.now().plusMinutes(otpExpiryMinutes))
                .verified(false)
                .build();
        otpRecordRepository.save(record);

        // In production, integrate with SMS gateway (MSG91, Fast2SMS, etc.)
        log.info("OTP for {}: {}", mobile, otp);

        return otp;
    }

    @Transactional
    public boolean verifyOtp(String mobile, String otp) {
        OtpRecord record = otpRecordRepository
                .findTopByMobileAndVerifiedFalseOrderByCreatedAtDesc(mobile)
                .orElseThrow(() -> new InvalidOtpException("No OTP found for this mobile number"));

        if (record.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new InvalidOtpException("OTP has expired. Please request a new one.");
        }

        if (!record.getOtpCode().equals(otp)) {
            throw new InvalidOtpException("Invalid OTP");
        }

        record.setVerified(true);
        otpRecordRepository.save(record);
        return true;
    }

    @Scheduled(fixedRate = 3600000) // every hour
    @Transactional
    public void cleanupExpiredOtps() {
        otpRecordRepository.deleteByExpiresAtBefore(LocalDateTime.now());
    }
}
