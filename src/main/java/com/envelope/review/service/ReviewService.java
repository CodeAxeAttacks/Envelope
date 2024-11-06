package com.envelope.review.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.envelope.exception.exceptions.InvalidInputException;
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
@Transactional(readOnly = true)
public class ReviewService {

    private final InstructorReviewRepository instructorReviewRepository;
    private final InstructorRepository instructorRepository;
    private final JwtService jwtService;

    public List<InstructorReviewDto> getAllInstructorReviews() {
        return instructorReviewRepository.findAll().stream()
            .map(instructorReview -> InstructorReviewDto.builder()
                .id(instructorReview.getId())
                .rate(instructorReview.getRate())
                .review(instructorReview.getReview())
                .userId(instructorReview.getUser().getId())
                .instructorId(instructorReview.getInstructor().getId())
                .build()).toList();
    }

    public InstructorReviewDto getInstructorReviewById(Long instructorReviewId) {
        if (instructorReviewId == null || instructorReviewId < 0) {
            String errorMessage = "Instructor review's id must not be null or negative";
            log.warn(errorMessage);
            throw new InvalidInputException(errorMessage);
        }

        Optional<InstructorReview> reviewOptional = instructorReviewRepository.findById(instructorReviewId);
        if (reviewOptional.isEmpty()) {
            String errorMessage = String.format("Instructor review with id %d does not exist", instructorReviewId);
            log.warn(errorMessage);
            throw new ObjectNotFoundException(errorMessage);
        }
        InstructorReview review = reviewOptional.get();

        return InstructorReviewDto.builder()
                    .id(review.getId())
                    .rate(review.getRate())
                    .review(review.getReview())
                    .userId(review.getUser().getId())
                    .instructorId(review.getInstructor().getId())
                    .build();
    }

    @Transactional(readOnly = false)
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
