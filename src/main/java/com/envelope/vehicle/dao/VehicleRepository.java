package com.envelope.vehicle.dao;

import com.envelope.driving_school.model.DrivingSchool;
import com.envelope.instructor.model.Instructor;
import com.envelope.vehicle.model.Category;
import com.envelope.vehicle.model.Transmission;
import com.envelope.vehicle.model.Vehicle;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findAllByInstructor(Instructor instructor);

    List<Vehicle> findAllByDrivingSchool(DrivingSchool drivindSchool);

    Optional<Vehicle> findByIdAndInstructor(Long id, Instructor instructor);

    Optional<Vehicle> findByIdAndDrivingSchool(Long id, DrivingSchool drivingSchool);

    @Query("SELECT DISTINCT v.category FROM Vehicle v JOIN v.instructor i WHERE i.id = :instructorId")
    List<Category> findAllCategoriesByInstructorId(@Param("instructorId") Long instructorId);

    @Query("SELECT DISTINCT v.transmission FROM Vehicle v JOIN v.instructor i WHERE i.id = :instructorId")
    List<Transmission> findAllTransmissionsByInstructorId(@Param("instructorId") Long instructorId);
}
