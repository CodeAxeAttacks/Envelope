package com.envelope.corporate_request.dto;

import com.envelope.course.model.VehicleCategory;
import com.envelope.vehicle.model.Transmission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class RegisterCorporateRequestDto {

    @NotBlank(message = "Field \"componyName\" must not be blank")
    @Size(max = 64, message = "Field's \"componyName\" length must be lower than 65 symbols")
    private String companyName;

    @NotNull(message = "Field \"employeeCount\" must not ne null")
    private Integer employeeCount;

    @NotBlank(message = "Field \"description\" must not be blank")
    @Size(max = 512, message = "Field's \"description\" length must be lower than 513 symbols")
    private String description;

    @NotNull(message = "Field \"vehicleCategory\" must not ne null")
    private VehicleCategory vehicleCategory;

    @NotNull(message = "Field \"Transmission\" must not ne null")
    private Transmission transmission;


}