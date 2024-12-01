package com.envelope.course.dao;

import com.envelope.course.model.Course;
import com.envelope.driving_school.model.DrivingSchool;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByIdAndDrivingSchool(Long id, DrivingSchool drivingSchool);
}
