package com.envelope.driving_school.dto.driving_school;

import java.time.Instant;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.envelope.driving_school.dto.additional_service.ResultAdditionalServiceDto;
import com.envelope.driving_school.dto.promotion.ResultPromotionDto;
import com.envelope.user.dto.UserDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class ResultDrivingSchoolDto {

    private Long id;

    private String name;

    private String address;

    private String description;

    private Float rating;

    @DateTimeFormat(pattern = "yyyy/MM/dd hh:mm:ss")
    private Instant createdAt;

    private List<UserDto> admins;
    
    private List<ResultAdditionalServiceDto> additionalServices;

    private List<ResultPromotionDto> promotions;
}
