package com.envelope.review.service;

import org.springframework.stereotype.Service;

import com.envelope.review.dao.InstructorReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InstructorReviewService {

    private final InstructorReviewRepository repository;

}
