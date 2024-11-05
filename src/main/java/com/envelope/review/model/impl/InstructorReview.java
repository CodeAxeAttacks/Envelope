package com.envelope.review.model.impl;

import com.envelope.instructor.model.Instructor;
import com.envelope.review.model.Review;
import com.envelope.user.model.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "instructor_reviews")
@Getter
@Setter
@ToString
public class InstructorReview extends Review {
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    public InstructorReview(Float rate, String review, User user, Instructor instructor) {
        super(null, rate, review, user);
        this.instructor = instructor;
    }
    
}
