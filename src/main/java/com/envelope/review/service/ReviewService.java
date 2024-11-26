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
import com.envelope.review.model.InstructorReview;
import com.envelope.security.JwtService;
import com.envelope.user.dto.UserDto;
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
                        .user(UserDto.builder()
                                .id(instructorReview.getUser().getId())
                                .email(instructorReview.getUser().getEmail())
                                .firstName(instructorReview.getUser().getFirstName())
                                .lastName(instructorReview.getUser().getLastName())
                                .phone(instructorReview.getUser().getPhone())
                                .createdAt(instructorReview.getUser().getCreatedAt())
                                .role(instructorReview.getUser().getRole())
                                .status(instructorReview.getUser().getStatus())
                                .instructorId(null)
                                .build())
                        .instructorId(instructorReview.getInstructor().getId())
                        .build())
                .toList();
    }

    public List<InstructorReviewDto> getReviewsByInstructorId(Long instructorId) {
        if (instructorId == null || instructorId < 0) {
            String errorMessage = "Instructor's id must not be null or negative";
            log.warn(errorMessage);
            throw new InvalidInputException(errorMessage);
        }

        Optional<Instructor> instructorOptional = instructorRepository.findById(instructorId);
        if (instructorOptional.isEmpty()) {
            String errorMessage = String.format("Instructor with id %d does not exist", instructorId);
            log.warn(errorMessage);
            throw new ObjectNotFoundException(errorMessage);
        }
        Instructor instructor = instructorOptional.get();

        return instructorReviewRepository.findAllByInstructor(instructor).stream()
                .map(instructorReview -> InstructorReviewDto.builder()
                        .id(instructorReview.getId())
                        .rate(instructorReview.getRate())
                        .review(instructorReview.getReview())
                        .user(UserDto.builder()
                                .id(instructorReview.getUser().getId())
                                .email(instructorReview.getUser().getEmail())
                                .firstName(instructorReview.getUser().getFirstName())
                                .lastName(instructorReview.getUser().getLastName())
                                .phone(instructorReview.getUser().getPhone())
                                .createdAt(instructorReview.getUser().getCreatedAt())
                                .role(instructorReview.getUser().getRole())
                                .status(instructorReview.getUser().getStatus())
                                .instructorId(null)
                                .build())
                        .instructorId(instructorReview.getInstructor().getId())
                        .build())
                .toList();
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
        User user = review.getUser();

        return InstructorReviewDto.builder()
                .id(review.getId())
                .rate(review.getRate())
                .review(review.getReview())
                .user(UserDto.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .phone(user.getPhone())
                        .createdAt(user.getCreatedAt())
                        .role(user.getRole())
                        .status(user.getStatus())
                        .instructorId(null)
                        .build())
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

        InstructorReview instructorReview = instructorReviewRepository.save(new InstructorReview(
                null,
                registerReviewDto.getRate(),
                registerReviewDto.getReview(),
                user,
                instructor));
        log.info("Instructor review registered: {}", instructorReview);

        return InstructorReviewDto.builder()
                .id(instructorReview.getId())
                .rate(instructorReview.getRate())
                .review(instructorReview.getReview())
                .user(UserDto.builder()
                        .id(instructorReview.getUser().getId())
                        .email(instructorReview.getUser().getEmail())
                        .firstName(instructorReview.getUser().getFirstName())
                        .lastName(instructorReview.getUser().getLastName())
                        .phone(instructorReview.getUser().getPhone())
                        .createdAt(instructorReview.getUser().getCreatedAt())
                        .role(instructorReview.getUser().getRole())
                        .status(instructorReview.getUser().getStatus())
                        .instructorId(null)
                        .build())
                .instructorId(instructorReview.getInstructor().getId())
                .build();
    }
}
