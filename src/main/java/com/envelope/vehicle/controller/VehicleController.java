package com.envelope.vehicle.controller;

import com.envelope.vehicle.dto.VehicleDto;
import com.envelope.vehicle.service.VehicleService;

import jakarta.validation.Valid;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
@Slf4j
public class VehicleController {

    private final VehicleService service;

    public List<VehicleDto> getAll() {
        log.info("Get all vehicles");
        return service.getALl();
    }

    @PostMapping
    public VehicleDto createVehicle(@RequestBody @Valid VehicleDto vehicleDto) {
        log.info("Creating vehicle: {}", vehicleDto);
        return service.createVehicle(vehicleDto);
    }
}