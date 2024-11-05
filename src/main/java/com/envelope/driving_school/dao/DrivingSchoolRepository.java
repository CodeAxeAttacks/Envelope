package com.envelope.driving_school.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.envelope.driving_school.model.DrivingSchool;

public interface DrivingSchoolRepository extends JpaRepository<DrivingSchool, Long> {
    
}
