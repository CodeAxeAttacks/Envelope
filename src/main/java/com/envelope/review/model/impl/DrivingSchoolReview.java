package com.envelope.review.model.impl;

import com.envelope.driving_school.model.DrivingSchool;
import com.envelope.review.model.Review;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "driving_school_reviews")
public class DrivingSchoolReview extends Review { 
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "instructor_id")
    private DrivingSchool drivingSchool;

}