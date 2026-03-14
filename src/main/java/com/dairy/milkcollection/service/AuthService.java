package com.dairy.milkcollection.service;

import com.dairy.milkcollection.dto.request.AdminLoginRequest;
import com.dairy.milkcollection.dto.request.SendOtpRequest;
import com.dairy.milkcollection.dto.request.VerifyOtpRequest;
import com.dairy.milkcollection.dto.response.AuthResponse;
import com.dairy.milkcollection.model.AdminUser;
import com.dairy.milkcollection.model.Farmer;
import com.dairy.milkcollection.repository.AdminUserRepository;
import com.dairy.milkcollection.repository.FarmerRepository;
import com.dairy.milkcollection.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AdminUserRepository adminUserRepository;
    private final FarmerRepository farmerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final OtpService otpService;

    public AuthResponse adminLogin(AdminLoginRequest request) {
        AdminUser admin = adminUserRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), admin.getPasswordHash())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        String token = tokenProvider.generateAdminToken(admin.getUsername(), admin.getRole().name());

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .role(admin.getRole().name())
                .name(admin.getUsername())
                .expiresIn(tokenProvider.getAdminExpirationMs())
                .build();
    }

    public void sendOtp(SendOtpRequest request) {
        otpService.generateAndSendOtp(request.getMobile());
    }

    public AuthResponse verifyOtp(VerifyOtpRequest request) {
        otpService.verifyOtp(request.getMobile(), request.getOtp());

        Farmer farmer = farmerRepository.findByMobile(request.getMobile())
                .orElseThrow(() -> new BadCredentialsException("Farmer not found"));

        String token = tokenProvider.generateFarmerToken(
                farmer.getId().toString(), farmer.getMobile());

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .role("FARMER")
                .name(farmer.getName())
                .farmerId(farmer.getId().toString())
                .expiresIn(tokenProvider.getFarmerExpirationMs())
                .build();
    }
}
