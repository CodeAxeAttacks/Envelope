package com.envelope.instructor.dto.instructor_service;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class InstructorServiceDto {
 
    private Long id;
    
    private String name;

    private String description;

    private Float price;

    private Long instructorId;

}
