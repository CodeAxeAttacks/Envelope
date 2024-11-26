package com.envelope.driving_school.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.envelope.driving_school.model.DrivingSchool;

public interface DrivingSchoolRepository extends JpaRepository<DrivingSchool, Long> {
    Optional<DrivingSchool> findByName(String name);
    Optional<DrivingSchool> findByEmail(String email);

    @Query("SELECT d FROM DrivingSchool d JOIN d.admins admin WHERE admin.id = :userId")
    List<DrivingSchool> findAllByUser(@Param("userId") Long userId);
}
