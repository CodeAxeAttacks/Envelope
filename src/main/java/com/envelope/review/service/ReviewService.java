package com.envelope.review.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.envelope.exception.exceptions.ObjectNotFoundException;
import com.envelope.instructor.dao.InstructorRepository;
import com.envelope.instructor.model.Instructor;
import com.envelope.review.dao.InstructorReviewRepository;
import com.envelope.review.dto.RegisterReviewDto;
import com.envelope.review.dto.instructor.InstructorReviewDto;
import com.envelope.review.model.impl.InstructorReview;
import com.envelope.security.JwtService;
import com.envelope.user.model.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final InstructorReviewRepository instructorReviewRepository;
    private final InstructorRepository instructorRepository;
    private final JwtService jwtService;

    public InstructorReviewDto registerInstructorReview(RegisterReviewDto registerReviewDto, Long instructorId) {
        User user = jwtService.currentUser();

        Optional<Instructor> instructorOptional = instructorRepository.findById(instructorId);
        if (instructorOptional.isEmpty()) {
            String errorMessage = String.format("Instructor with id %d does not exist", instructorId);
            log.warn(errorMessage);
            throw new ObjectNotFoundException(errorMessage);
        }
        Instructor instructor = instructorOptional.get();
        
        InstructorReview review = instructorReviewRepository.save(new InstructorReview(
            registerReviewDto.getRate(),
            registerReviewDto.getReview(),
            user,
            instructor));
        log.info("Instructor review registered: {}", review);

        return InstructorReviewDto.builder()
                .id(review.getId())
                .rate(review.getRate())
                .review(review.getReview())
                .userId(review.getUser().getId())
                .instructorId(review.getInstructor().getId())
                .build();
    }

}
