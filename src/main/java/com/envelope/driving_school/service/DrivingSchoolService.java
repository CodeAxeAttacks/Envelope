package com.envelope.driving_school.service;

import org.springframework.stereotype.Service;

import com.envelope.driving_school.dao.AdditionalServiceRepository;
import com.envelope.driving_school.dao.DrivingSchoolRepository;
import com.envelope.driving_school.dao.PromotionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DrivingSchoolService {

    private final DrivingSchoolRepository drivingSchoolRepository;
    private final AdditionalServiceRepository additionalServiceRepository;
    private final PromotionRepository promotionRepository;

}

