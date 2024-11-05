package com.envelope.driving_school.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.envelope.driving_school.dao.AdditionalServiceRepository;
import com.envelope.driving_school.dao.DrivingSchoolRepository;
import com.envelope.driving_school.dao.PromotionRepository;
import com.envelope.driving_school.dto.additional_service.AddAdditionalServiceDto;
import com.envelope.driving_school.dto.additional_service.AdditionalServiceDto;
import com.envelope.driving_school.dto.driving_school.DrivingSchoolDto;
import com.envelope.driving_school.dto.driving_school.RegisterDrivingSchoolDto;
import com.envelope.driving_school.dto.promotion.AddPromotionDto;
import com.envelope.driving_school.dto.promotion.PromotionDto;
import com.envelope.driving_school.model.AdditionalService;
import com.envelope.driving_school.model.DrivingSchool;
import com.envelope.driving_school.model.Promotion;
import com.envelope.exception.exceptions.NoAccessException;
import com.envelope.exception.exceptions.ObjectAlreadyExistsException;
import com.envelope.exception.exceptions.ObjectNotFoundException;
import com.envelope.security.JwtService;
import com.envelope.user.model.Role;
import com.envelope.user.model.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DrivingSchoolService {

    private final DrivingSchoolRepository drivingSchoolRepository;
    private final AdditionalServiceRepository additionalServiceRepository;
    private final PromotionRepository promotionRepository;
    private final JwtService jwtService;

    public DrivingSchoolDto register(RegisterDrivingSchoolDto registerDrivingSchoolDto) {
        User user = jwtService.currentUser();

        if (drivingSchoolRepository.findByName(registerDrivingSchoolDto.getName()).isPresent()) {
            String errorMessage = String.format("Driving school with name %s already exists",
                    registerDrivingSchoolDto.getName());
            log.warn(errorMessage);
            throw new ObjectAlreadyExistsException(errorMessage);
        }
        if (drivingSchoolRepository.findByEmail(registerDrivingSchoolDto.getEmail()).isPresent()) {
            String errorMessage = String.format("Driving school with email %s already exists",
                    registerDrivingSchoolDto.getEmail());
            log.warn(errorMessage);
            throw new ObjectAlreadyExistsException(errorMessage);
        }

        DrivingSchool drivingSchool = drivingSchoolRepository.save(
                DrivingSchool.builder()
                        .name(registerDrivingSchoolDto.getName())
                        .address(registerDrivingSchoolDto.getAddress())
                        .phoneNumber(registerDrivingSchoolDto.getPhoneNumber())
                        .email(registerDrivingSchoolDto.getEmail())
                        .description(registerDrivingSchoolDto.getDescription())
                        .rating(0f)
                        .admins(List.of(user))
                        .instructors(Collections.emptyList())
                        .build());
        log.info("Driving school registered: {}", drivingSchool);

        return DrivingSchoolDto.builder()
                .id(drivingSchool.getId())
                .name(drivingSchool.getName())
                .address(drivingSchool.getAddress())
                .description(drivingSchool.getDescription())
                .rating(drivingSchool.getRating())
                .createdAt(drivingSchool.getCreatedAt())
                .adminIds(drivingSchool.getAdmins().stream()
                        .map(admin -> admin.getId())
                        .toList())
                .build();
    }

    public AdditionalServiceDto addAdditionalService(AddAdditionalServiceDto addAdditionalServiceDto,
            Long drivingSchoolId) {
        User user = jwtService.currentUser();

        if (user.getRole() != Role.ADMINISTRATOR && user.getRole() != Role.INSTRUCTOR_AND_ADMINISTRATOR) {
            String errorMessage = String.format("User with id %d is not an administrator", user.getId());
            log.warn(errorMessage);
            throw new NoAccessException(errorMessage);
        }

        Optional<DrivingSchool> drivingSchoolOptional = drivingSchoolRepository.findById(drivingSchoolId);
        if (drivingSchoolOptional.isEmpty()) {
            String errorMessage = String.format("Driving school with id %d does not exits", drivingSchoolId);
            log.warn(errorMessage);
            throw new ObjectNotFoundException(errorMessage);
        }
        DrivingSchool drivingSchool = drivingSchoolOptional.get();

        AdditionalService service = additionalServiceRepository.save(
                AdditionalService.builder()
                        .name(addAdditionalServiceDto.getName())
                        .description(addAdditionalServiceDto.getDescription())
                        .price(addAdditionalServiceDto.getPrice())
                        .drivingSchool(drivingSchool)
                        .build());
        log.info("Additional service registered: {}", service);

        return AdditionalServiceDto.builder()
                .id(service.getId())
                .name(service.getName())
                .description(service.getDescription())
                .price(service.getPrice())
                .drivingSchoolId(service.getDrivingSchool().getId())
                .build();
    }

    public PromotionDto addPromotion(AddPromotionDto addPromotionDto, Long drivingSchoolId) {
        User user = jwtService.currentUser();

        if (user.getRole() != Role.ADMINISTRATOR && user.getRole() != Role.INSTRUCTOR_AND_ADMINISTRATOR) {
            String errorMessage = String.format("User with id %d is not an administrator", user.getId());
            log.warn(errorMessage);
            throw new NoAccessException(errorMessage);
        }

        Optional<DrivingSchool> drivingSchoolOptional = drivingSchoolRepository.findById(drivingSchoolId);
        if (drivingSchoolOptional.isEmpty()) {
            String errorMessage = String.format("Driving school with id %d does not exits", drivingSchoolId);
            log.warn(errorMessage);
            throw new ObjectNotFoundException(errorMessage);
        }
        DrivingSchool drivingSchool = drivingSchoolOptional.get();

        Promotion service = promotionRepository.save(
                Promotion.builder()
                        .name(addPromotionDto.getName())
                        .description(addPromotionDto.getDescription())
                        .start(addPromotionDto.getStart())
                        .finish(addPromotionDto.getFinish())
                        .drivingSchool(drivingSchool)
                        .build());
        log.info("Additional service registered: {}", service);

        return PromotionDto.builder()
                .id(service.getId())
                .name(service.getName())
                .description(service.getDescription())
                .start(service.getStart())
                .finish(service.getFinish())
                .drivingSchoolId(service.getDrivingSchool().getId())
                .build();
    }
}
