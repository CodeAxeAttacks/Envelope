package com.envelope.vehicle.service;

import com.envelope.vehicle.dto.VehicleDto;
import com.envelope.vehicle.model.Vehicle;
import com.envelope.driving_school.dao.DrivingSchoolRepository;
import com.envelope.driving_school.model.DrivingSchool;
import com.envelope.exception.exceptions.NoAccessException;
import com.envelope.exception.exceptions.ObjectNotFoundException;
import com.envelope.instructor.dao.InstructorRepository;
import com.envelope.instructor.model.Instructor;
import com.envelope.security.JwtService;
import com.envelope.user.model.Role;
import com.envelope.user.model.User;
import com.envelope.vehicle.dao.VehicleRepository;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final InstructorRepository instructorRepository;
    private final DrivingSchoolRepository drivingSchoolRepository;
    private final JwtService jwtService;

    public List<VehicleDto> getALl() {
        return vehicleRepository.findAll().stream()
                .map(vehicle -> VehicleDto.builder()
                        .id(vehicle.getId())
                        .model(vehicle.getModel())
                        .year(vehicle.getYear())
                        .transmission(vehicle.getTransmission())
                        .category(vehicle.getCategory())
                        .build())
                .toList();
    }

    public List<VehicleDto> getAllByInstructorId(Long id) {
        Optional<Instructor> instructorOptional = instructorRepository.findById(id);
        if (instructorOptional.isEmpty()) {
            String errorMessage = String.format("Instructor with id %d does not exist", id);
            log.warn(errorMessage);
            throw new ObjectNotFoundException(errorMessage);
        }
        Instructor instructor = instructorOptional.get();

        List<Vehicle> vehicles = vehicleRepository.findAllByInstructor(instructor);

        return vehicles.stream()
                .map(vehicle -> VehicleDto.builder()
                        .id(vehicle.getId())
                        .model(vehicle.getModel())
                        .year(vehicle.getYear())
                        .transmission(vehicle.getTransmission())
                        .category(vehicle.getCategory())
                        .build())
                .toList();
    }

    public List<VehicleDto> getAllByDrivingSchoolId(Long id) {
        Optional<DrivingSchool> drivingSchoolOptional = drivingSchoolRepository.findById(id);
        if (drivingSchoolOptional.isEmpty()) {
            String errorMessage = String.format("Driving school with id %d does not exist", id);
            log.warn(errorMessage);
            throw new ObjectNotFoundException(errorMessage);
        }
        DrivingSchool drivingSchool = drivingSchoolOptional.get();

        List<Vehicle> vehicles = vehicleRepository.findAllByDrivingSchool(drivingSchool);

        return vehicles.stream()
                .map(vehicle -> VehicleDto.builder()
                        .id(vehicle.getId())
                        .model(vehicle.getModel())
                        .year(vehicle.getYear())
                        .transmission(vehicle.getTransmission())
                        .category(vehicle.getCategory())
                        .build())
                .toList();
    }

    @Transactional(readOnly = false)
    public VehicleDto createInstructorVehicle(VehicleDto vehicleDto) {
        User currentUser = jwtService.currentUser();
        if (currentUser.getRole() != Role.INSTRUCTOR && currentUser.getRole() != Role.INSTRUCTOR_AND_ADMINISTRATOR) {
            String errorMessage = String.format("User with id %d is not an instructor", currentUser.getId());
            log.warn(errorMessage);
            throw new NoAccessException(errorMessage);
        }

        Instructor instructor = instructorRepository.findByUser(currentUser).get();

        Vehicle vehicle = Vehicle.builder()
                .model(vehicleDto.getModel())
                .year(vehicleDto.getYear())
                .transmission((vehicleDto.getTransmission()))
                .category(vehicleDto.getCategory())
                .instructor(instructor)
                .build();

        vehicle = vehicleRepository.save(vehicle);
        log.info("Created vehicle: {}", vehicle);

        return VehicleDto.builder()
                .id(vehicle.getId())
                .model(vehicle.getModel())
                .year(vehicle.getYear())
                .transmission(vehicle.getTransmission())
                .category(vehicle.getCategory())
                .build();
    }

    @Transactional(readOnly = false)
    public void deleteInstructorVehicle(Long id) {
        User currentUser = jwtService.currentUser();
        if (currentUser.getRole() != Role.INSTRUCTOR && currentUser.getRole() != Role.INSTRUCTOR_AND_ADMINISTRATOR) {
            String errorMessage = String.format("User with id %d is not an instructor", currentUser.getId());
            log.warn(errorMessage);
            throw new NoAccessException(errorMessage);
        }

        Instructor instructor = instructorRepository.findByUser(currentUser).get();
        Optional<Vehicle> vehicleOptional = vehicleRepository.findByIdAndInstructor(id, instructor);

        if (vehicleOptional.isEmpty()) {
            String errorMessage = String.format("Instructor with id %d does not own vehicle with id %d",
                    instructor.getId(), id);
            log.warn(errorMessage);
            throw new ObjectNotFoundException(errorMessage);
        }
        Vehicle vehicle = vehicleOptional.get();

        if (!vehicle.getInstructor().getUser().getId().equals(currentUser.getId())) {
            String errorMessage = String.format("User with id %d is not an owner of vehicle with id %d",
                    currentUser.getId(), id);
            log.warn(errorMessage);
            throw new NoAccessException(errorMessage);
        }

        vehicleRepository.delete(vehicle);
    }

    @Transactional(readOnly = false)
    public VehicleDto createDrivingSchoolVehicle(Long id, VehicleDto vehicleDto) {
        User currentUser = jwtService.currentUser();
        if (currentUser.getRole() != Role.ADMINISTRATOR && currentUser.getRole() != Role.INSTRUCTOR_AND_ADMINISTRATOR) {
            String errorMessage = String.format("User with id %d is not an administrator", currentUser.getId());
            log.warn(errorMessage);
            throw new NoAccessException(errorMessage);
        }

        Optional<DrivingSchool> drivingSchoolOptional = drivingSchoolRepository.findById(id);
        if (drivingSchoolOptional.isEmpty()) {
            String errorMessage = String.format("Driving school with id %d does not exist", id);
            log.warn(errorMessage);
            throw new ObjectNotFoundException(errorMessage);
        }
        DrivingSchool drivingSchool = drivingSchoolOptional.get();

        User user = null;
        for (User admin : drivingSchool.getAdmins()) {
            if (admin.getId().equals(currentUser.getId())) {
                user = admin;
                break;
            }
        }
        if (user == null) {
            String errorMessage = String.format("User with id %d is not an administrator for school with id %d",
                    currentUser.getId(), id);
            log.warn(errorMessage);
            throw new NoAccessException(errorMessage);
        }

        Vehicle vehicle = Vehicle.builder()
                .model(vehicleDto.getModel())
                .year(vehicleDto.getYear())
                .transmission((vehicleDto.getTransmission()))
                .category(vehicleDto.getCategory())
                .drivingSchool(drivingSchool)
                .build();

        vehicle = vehicleRepository.save(vehicle);
        log.info("Created vehicle: {}", vehicle);

        return VehicleDto.builder()
                .id(vehicle.getId())
                .model(vehicle.getModel())
                .year(vehicle.getYear())
                .transmission(vehicle.getTransmission())
                .category(vehicle.getCategory())
                .build();
    }

    @Transactional(readOnly = false)
    public void deleteDrivingSchoolVehicle(Long drivingSchoolId, Long vehicleId) {
        User currentUser = jwtService.currentUser();
        if (currentUser.getRole() != Role.ADMINISTRATOR && currentUser.getRole() != Role.INSTRUCTOR_AND_ADMINISTRATOR) {
            String errorMessage = String.format("User with id %d is not an administrator", currentUser.getId());
            log.warn(errorMessage);
            throw new NoAccessException(errorMessage);
        }

        Optional<DrivingSchool> drivingSchoolOptional = drivingSchoolRepository.findById(drivingSchoolId);
        if (drivingSchoolOptional.isEmpty()) {
            String errorMessage = String.format("Driving school with id %d does not exist", drivingSchoolId);
            log.warn(errorMessage);
            throw new ObjectNotFoundException(errorMessage);
        }
        DrivingSchool drivingSchool = drivingSchoolOptional.get();

        User user = null;
        for (User admin : drivingSchool.getAdmins()) {
            if (admin.getId().equals(currentUser.getId())) {
                user = admin;
                break;
            }
        }
        if (user == null) {
            String errorMessage = String.format("User with id %d is not an administrator for school with id %d",
                    currentUser.getId(), drivingSchoolId);
            log.warn(errorMessage);
            throw new NoAccessException(errorMessage);
        }

        Optional<Vehicle> vehicleOptional = vehicleRepository.findByIdAndDrivingSchool(vehicleId, drivingSchool);
        if (vehicleOptional.isEmpty()) {
            String errorMessage = String.format("Driving school with id %d does not have a vehicle with id %d", drivingSchoolId, vehicleId);
            log.warn(errorMessage);
            throw new ObjectNotFoundException(errorMessage);
        }
        Vehicle vehicle = vehicleOptional.get();

        vehicleRepository.delete(vehicle);
    }

}