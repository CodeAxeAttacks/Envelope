package com.envelope.instructor.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.envelope.instructor.model.Instructor;
import com.envelope.instructor.model.InstructorService;

@Repository
public interface InstructorServiceRepository extends JpaRepository<InstructorService, Long> {
    List<InstructorService> findAllByInstructor(Instructor instructor);
}
