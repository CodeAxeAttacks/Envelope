package com.envelope.driving_school.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.envelope.driving_school.model.AdditionalService;
import com.envelope.driving_school.model.DrivingSchool;

public interface AdditionalServiceRepository extends JpaRepository<AdditionalService, Long> {
    
    List<AdditionalService> findAllByDrivingSchool(DrivingSchool drivingSchool);

    Optional<AdditionalService> findByIdAndDrivingSchool(Long id, DrivingSchool drivingSchool);

}
