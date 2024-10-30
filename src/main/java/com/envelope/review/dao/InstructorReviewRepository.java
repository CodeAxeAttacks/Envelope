package com.envelope.review.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.envelope.review.model.impl.InstructorReview;

@Repository
public interface InstructorReviewRepository extends JpaRepository<InstructorReview, Long> {
}
