package com.envelope.corporate_request.service;

import org.springframework.stereotype.Service;

import com.envelope.corporate_request.dao.CorporateRequestRepository;
import com.envelope.corporate_request.dto.CorporateRequestDto;
import com.envelope.corporate_request.dto.RegisterCorporateRequestDto;
import com.envelope.corporate_request.model.CorporateRequest;
import com.envelope.security.JwtService;
import com.envelope.user.model.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CorporateRequestService {

    private final CorporateRequestRepository repository;
    private final JwtService jwtService;

    public CorporateRequestDto register(RegisterCorporateRequestDto registerCorporateRequestDto) {
        User user = jwtService.currentUser();

        CorporateRequest request = repository.save(
                CorporateRequest.builder()
                        .companyName(registerCorporateRequestDto.getCompanyName())
                        .employeeCount(registerCorporateRequestDto.getEmployeeCount())
                        .description(registerCorporateRequestDto.getDescription())
                        .vehicleCategory(registerCorporateRequestDto.getVehicleCategory())
                        .transmissionType(registerCorporateRequestDto.getTransmissionType())
                        .user(user)
                        .build());
        log.info("Corporate request registered: {}", request);

        return CorporateRequestDto.builder()
                .id(request.getId())
                .companyName(request.getCompanyName())
                .employeeCount(request.getEmployeeCount())
                .createdAt(request.getCreatedAt())
                .description(request.getDescription())
                .vehicleCategory(request.getVehicleCategory())
                .transmissionType(request.getTransmissionType())
                .userId(request.getUser().getId())
                .build();
    }

}