package com.envelope.review.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.envelope.instructor.model.Instructor;
import com.envelope.review.model.InstructorReview;

@Repository
public interface InstructorReviewRepository extends JpaRepository<InstructorReview, Long> {
    public List<InstructorReview> findAllByInstructor(Instructor instructor);
}
