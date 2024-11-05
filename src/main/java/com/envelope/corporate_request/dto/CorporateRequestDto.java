package com.envelope.corporate_request.dto;

import java.time.Instant;

import org.springframework.format.annotation.DateTimeFormat;

import com.envelope.course.model.VehicleCategory;
import com.envelope.vehicle.model.TransmissionType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class CorporateRequestDto {

    private Long id;

    private String companyName;

    private Integer employeeCount;
    
    @DateTimeFormat(pattern = "yyyy/MM/dd hh:mm:ss")
    private Instant createdAt;

    private String description;

    private VehicleCategory vehicleCategory;

    private TransmissionType transmissionType;

    private Long userId;

}