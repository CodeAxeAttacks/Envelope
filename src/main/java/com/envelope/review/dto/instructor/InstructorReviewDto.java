package com.envelope.review.dto.instructor;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class InstructorReviewDto {

    private Long id;

    private Float rate;

    private String review;

    private Long userId;

    private Long instructorId;

}
