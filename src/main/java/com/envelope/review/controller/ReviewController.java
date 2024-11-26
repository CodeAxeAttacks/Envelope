package com.envelope.review.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.envelope.review.dto.RegisterReviewDto;
import com.envelope.review.dto.instructor.InstructorReviewDto;
import com.envelope.review.service.ReviewService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService service;

    @GetMapping("/instructor/review")
    public List<InstructorReviewDto> getAllInstructorReviews() {
        log.info("Getting all instructor reviews");
        return service.getAllInstructorReviews();
    }

    @GetMapping("/instructor/{instructorId}/review")
    public List<InstructorReviewDto> getReviewsByInstructorId(@PathVariable("instructorId") Long instructorId) {
        log.info("Getting all reviews for instructor with id: {}", instructorId);
        return service.getReviewsByInstructorId(instructorId);
    }

    @GetMapping("/instructor/review/{id}")
    public InstructorReviewDto getInstructorReviewById(@PathVariable(name = "id") Long instructorReviewId) {
        log.info("Getting an instructor review with id: {}", instructorReviewId);
        return service.getInstructorReviewById(instructorReviewId);
    }

    @PostMapping("/instructor/{id}/review")
    public InstructorReviewDto registerInstructorReview(
        @RequestBody @Valid RegisterReviewDto registerReviewDto,
        @PathVariable(name = "id") Long instructorId
    ) {
        log.info("Register review to instructor with id {}: {}", instructorId, registerReviewDto);
        return service.registerInstructorReview(registerReviewDto, instructorId);
    }
    
}
