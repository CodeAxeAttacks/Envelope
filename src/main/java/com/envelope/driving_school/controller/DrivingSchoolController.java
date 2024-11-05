package com.envelope.driving_school.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.envelope.driving_school.dto.additional_service.AddAdditionalServiceDto;
import com.envelope.driving_school.dto.additional_service.AdditionalServiceDto;
import com.envelope.driving_school.dto.driving_school.DrivingSchoolDto;
import com.envelope.driving_school.dto.driving_school.RegisterDrivingSchoolDto;
import com.envelope.driving_school.dto.promotion.AddPromotionDto;
import com.envelope.driving_school.dto.promotion.PromotionDto;
import com.envelope.driving_school.service.DrivingSchoolService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/driving-school")
@RequiredArgsConstructor
@Slf4j
public class DrivingSchoolController {

    private final DrivingSchoolService service;

    @PostMapping
    public DrivingSchoolDto register(@RequestBody @Valid RegisterDrivingSchoolDto registerDrivingSchoolDto) {
        log.info("Register driving school: {}", registerDrivingSchoolDto);
        return service.register(registerDrivingSchoolDto);
    }

    @PostMapping("/{id}/additional-service")
    public AdditionalServiceDto addAdditionalService(
            @RequestBody @Valid AddAdditionalServiceDto addAdditionalServiceDto,
            @PathVariable(name = "id") Long drivingSchoolId) {
        log.info("Add additional service to driving school with id {}: {}", drivingSchoolId, addAdditionalServiceDto);
        return service.addAdditionalService(addAdditionalServiceDto, drivingSchoolId);
    }

    @PostMapping("/{id}/promotion")
    public PromotionDto addPromotion(
            @RequestBody @Valid AddPromotionDto addPromotionDto,
            @PathVariable(name = "id") Long drivingSchoolId) {
        log.info("Add promotion to driving school with id {}: {}", drivingSchoolId, addPromotionDto);
        return service.addPromotion(addPromotionDto, drivingSchoolId);
    }
}
