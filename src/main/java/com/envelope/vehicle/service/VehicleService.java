package com.envelope.vehicle.service;

import com.envelope.vehicle.dto.VehicleDto;
import com.envelope.vehicle.model.Vehicle;
import com.envelope.vehicle.dao.VehicleRepository;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleDto createVehicle(VehicleDto vehicleDto) {
        Vehicle vehicle = Vehicle.builder()
            .model(vehicleDto.getModel())
            .year(vehicleDto.getYear())
            .transmissionType((vehicleDto.getTransmissionType()))
            .build();

        vehicle = vehicleRepository.save(vehicle);
        log.info("Created vehicle: {}", vehicle);

        return VehicleDto.builder()
            .id(vehicle.getId())
            .model(vehicle.getModel())
            .year(vehicle.getYear())
            .transmissionType(vehicle.getTransmissionType())
            .build();
    }
    
}