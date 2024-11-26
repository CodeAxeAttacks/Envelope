package com.envelope.review.dto.instructor;

import com.envelope.user.dto.UserDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class InstructorReviewDto {

    private Long id;

    private Float rate;

    private String review;

    private UserDto user;

    private Long instructorId;

}
