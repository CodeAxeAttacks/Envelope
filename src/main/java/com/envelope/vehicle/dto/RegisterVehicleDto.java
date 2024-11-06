package com.envelope.vehicle.dto;

import com.envelope.course.model.VehicleCategory;
import com.envelope.vehicle.model.TransmissionType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class RegisterVehicleDto {
    
    @NotBlank(message = "Field \"model\" must not be blank")
    @Size(max = 64, message = "Field's \"model\" length must be lower than 65 symbols")
    private String model;

    @NotNull(message = "Field \"year\" must not be null")
    private Integer year;

    @NotNull(message = "Field \"transmissionType\" must not be null")
    private TransmissionType transmissionType;

    @NotNull(message = "Field \"vehicleCategory\" must not be null")
    private VehicleCategory vehicleCategory;

}