package com.envelope.vehicle.dto;

import com.envelope.vehicle.model.Category;
import com.envelope.vehicle.model.Transmission;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class VehicleDto {
    
    private Long id;

    private String model;

    private Integer year;

    private Transmission transmission;

    private Category category;
}
