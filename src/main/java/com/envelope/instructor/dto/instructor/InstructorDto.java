package com.envelope.instructor.dto.instructor;

import com.envelope.user.dto.UserDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class InstructorDto {
   
    private Long id;

    private UserDto user;

    private Integer experience;

    private String description;

    private Float rating;
    
}
