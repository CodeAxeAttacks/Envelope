package com.envelope.vehicle.service;

import com.envelope.vehicle.dto.VehicleDto;
import com.envelope.vehicle.model.Vehicle;
import com.envelope.vehicle.dao.VehicleRepository;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public List<VehicleDto> getALl() {
        return vehicleRepository.findAll().stream()
            .map(vehicle -> VehicleDto.builder()
                    .id(vehicle.getId())
                    .model(vehicle.getModel())
                    .year(vehicle.getYear())
                    .transmissionType(vehicle.getTransmissionType())
                    .build()).toList();
    }

    @Transactional(readOnly = false)
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