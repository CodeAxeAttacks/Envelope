package com.envelope.driving_school.dto.driving_school;

import java.time.Instant;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class DrivingSchoolDto {

    private Long id;

    private String name;

    private String address;

    private String description;

    private Float rating;

    @DateTimeFormat(pattern = "yyyy/MM/dd hh:mm:ss")
    private Instant createdAt;

    private List<Long> adminIds;
    
    private List<Long> additionalServiceIds;

    private List<Long> promotionIds;
}
