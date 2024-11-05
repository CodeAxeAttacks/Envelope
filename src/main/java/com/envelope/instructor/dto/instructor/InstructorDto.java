package com.envelope.instructor.dto.instructor;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class InstructorDto {
   
    private Long id;

    private Long userId;

    private Integer experience;

    private String description;

    private Float rating;

    private List<Long> serviceIds;

    private List<Long> vehicleIds;
    
}
