package com.envelope.vehicle.dto;

import com.envelope.vehicle.model.TransmissionType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class VehicleDto {
    
    private Long id;

    private String model;

    private Integer year;

    private TransmissionType transmissionType;

}
