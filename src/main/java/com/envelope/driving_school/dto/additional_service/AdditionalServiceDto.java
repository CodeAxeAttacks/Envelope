package com.envelope.driving_school.dto.additional_service;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class AdditionalServiceDto {

    private Long id;

    private String name;

    private String description;

    private Float price;

    private Long drivingSchoolId;
    
}
