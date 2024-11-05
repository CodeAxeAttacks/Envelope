package com.envelope.instructor.dto;

import com.envelope.user.dto.UserDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class ResultInstructorDto {
   
    private Long id;

    private UserDto user;

    private Integer experience;

    private String description;

    private Float rating;
}
