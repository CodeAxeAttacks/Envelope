package com.envelope.driving_school.dto.promotion;

import java.time.Instant;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class PromotionDto {
    
    private Long id;

    private String name;

    private String description;

    @DateTimeFormat(pattern = "yyyy/MM/dd hh:mm:ss")
    private Instant start;

    @DateTimeFormat(pattern = "yyyy/MM/dd hh:mm:ss")
    private Instant finish;

    private Long drivingSchoolId;

}
