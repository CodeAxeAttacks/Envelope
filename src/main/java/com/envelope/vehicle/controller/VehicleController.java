package com.envelope.vehicle.controller;

import com.envelope.vehicle.dto.VehicleDto;
import com.envelope.vehicle.service.VehicleService;

import jakarta.validation.Valid;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequiredArgsConstructor
@Slf4j
public class VehicleController {

    private final VehicleService service;

    public List<VehicleDto> getAll() {
        log.info("Get all vehicles");
        return service.getALl();
    }
    
    @GetMapping("/instructor/{id}/vehicle")
    public List<VehicleDto> getAllByInstructorId(@PathVariable("id") Long id) {
        log.info("Get all vehicles for instructor with id: {}", id);
        return service.getAllByInstructorId(id);
    }

    @GetMapping("/driving-school/{id}/vehicle")
    public List<VehicleDto> getAllByDrivingSchoolId(@PathVariable("id") Long id) {
        log.info("Get all vehicles for driving school with id: {}", id);
        return service.getAllByDrivingSchoolId(id);
    }

    @PostMapping("/instructor/vehicle")
    public VehicleDto createInstructorVehicle(@RequestBody @Valid VehicleDto vehicleDto) {
        log.info("Creating vehicle: {}", vehicleDto);
        return service.createInstructorVehicle(vehicleDto);
    }

    @PostMapping("/driving-school/{id}/vehicle")
    public VehicleDto createDrivingSchoolVehicle(@PathVariable("id") Long id, @RequestBody @Valid VehicleDto vehicleDto) {
        log.info("Creating vehicle: {}", vehicleDto);
        return service.createDrivingSchoolVehicle(id, vehicleDto);
    }

    @DeleteMapping("/instructor/vehicle/{id}")
    public void deleteInstructorVehicle(@PathVariable("id") Long id) {
        log.info("Deleting vehicle with id: {}", id);
        service.deleteInstructorVehicle(id);
    }

    @DeleteMapping("/driving-school/{drivingSchoolId}/vehicle/{vehicleId}")
    public void deleteDrivingSchoolVehicle(@PathVariable("drivingSchoolId") Long drivingSchoolId, @PathVariable("vehicleId") Long vehicleId) {
        log.info("Deleting vehicle with id {} in driving school with id {}", vehicleId, drivingSchoolId);
        service.deleteDrivingSchoolVehicle(drivingSchoolId, vehicleId);
    }

}