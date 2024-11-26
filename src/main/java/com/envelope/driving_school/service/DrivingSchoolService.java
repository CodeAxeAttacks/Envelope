package com.envelope.driving_school.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.envelope.driving_school.dao.AdditionalServiceRepository;
import com.envelope.driving_school.dao.DrivingSchoolRepository;
import com.envelope.driving_school.dao.PromotionRepository;
import com.envelope.driving_school.dto.additional_service.AddAdditionalServiceDto;
import com.envelope.driving_school.dto.additional_service.AdditionalServiceDto;
import com.envelope.driving_school.dto.driving_school.DrivingSchoolDto;
import com.envelope.driving_school.dto.driving_school.PatchDrivingSchoolDto;
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
import com.envelope.user.dao.UserRepository;
import com.envelope.user.model.Role;
import com.envelope.user.model.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DrivingSchoolService {

    private final DrivingSchoolRepository drivingSchoolRepository;
    private final AdditionalServiceRepository additionalServiceRepository;
    private final PromotionRepository promotionRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public List<DrivingSchoolDto> getAll() {
        return drivingSchoolRepository.findAll().stream()
                .map(drivingSchool -> DrivingSchoolDto.builder()
                        .id(drivingSchool.getId())
                        .name(drivingSchool.getName())
                        .address(drivingSchool.getAddress())
                        .description(drivingSchool.getDescription())
                        .rating(drivingSchool.getRating())
                        .createdAt(drivingSchool.getCreatedAt())
                        .adminIds(drivingSchool.getAdmins().stream()
                                .map(admin -> admin.getId())
                                .toList())
                        .build())
                .toList();
    }

    @Transactional(readOnly = true)
    public DrivingSchoolDto getById(Long id) {
        Optional<DrivingSchool> drivingSchoolOptional = drivingSchoolRepository.findById(id);
        if (drivingSchoolOptional.isEmpty()) {
            String errorMessage = String.format("Driving school with id %d does not exist", id);
            log.warn(errorMessage);
            throw new ObjectNotFoundException(errorMessage);
        }
        DrivingSchool drivingSchool = drivingSchoolOptional.get();

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

    @Transactional(readOnly = true)
    public List<DrivingSchoolDto> getAllByUserId(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            String errorMessage = String.format("User with id %d does not exits", id);
            log.warn(errorMessage);
            throw new ObjectNotFoundException(errorMessage);
        }
        User user = userOptional.get();

        List<DrivingSchool> drivingSchools = drivingSchoolRepository.findAllByUser(user.getId());

        return drivingSchools.stream()
                .map(drivingSchool -> DrivingSchoolDto.builder()
                        .id(drivingSchool.getId())
                        .name(drivingSchool.getName())
                        .address(drivingSchool.getAddress())
                        .description(drivingSchool.getDescription())
                        .rating(drivingSchool.getRating())
                        .createdAt(drivingSchool.getCreatedAt())
                        .adminIds(drivingSchool.getAdmins().stream()
                                .map(admin -> admin.getId())
                                .toList())
                        .build())
                .toList();
    }

    @Transactional
    public List<AdditionalServiceDto> getAllServicesById(Long id) {
        Optional<DrivingSchool> drivingSchoolOptional = drivingSchoolRepository.findById(id);
        if (drivingSchoolOptional.isEmpty()) {
            String errorMessage = String.format("Driving school with id %d does not exist", id);
            log.warn(errorMessage);
            throw new ObjectAlreadyExistsException(errorMessage);
        }
        DrivingSchool drivingSchool = drivingSchoolOptional.get();

        List<AdditionalService> services = additionalServiceRepository.findAllByDrivingSchool(drivingSchool);

        return services.stream()
                .map(service -> AdditionalServiceDto.builder()
                        .id(service.getId())
                        .name(service.getName())
                        .description(service.getDescription())
                        .price(service.getPrice())
                        .drivingSchoolId(service.getDrivingSchool().getId())
                        .build())
                .toList();
    }

    @Transactional(readOnly = false)
    public DrivingSchoolDto register(RegisterDrivingSchoolDto registerDrivingSchoolDto) {
        User user = jwtService.currentUser();

        System.out.println(user.getId());

        if (drivingSchoolRepository.findByEmail(registerDrivingSchoolDto.getEmail()).isPresent()) {
            String errorMessage = String.format("Driving school with email %s already exists",
                    registerDrivingSchoolDto.getEmail());
            log.warn(errorMessage);
            throw new ObjectAlreadyExistsException(errorMessage);
        }

        if (user.getRole() == Role.USER)
            user.setRole(Role.ADMINISTRATOR);
        else if (user.getRole() == Role.INSTRUCTOR)
            user.setRole(Role.INSTRUCTOR_AND_ADMINISTRATOR);

        user = userRepository.save(user);
        System.out.println(user.getId());
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

    @Transactional(readOnly = false)
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

    @Transactional(readOnly = false)
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

    @Transactional(readOnly = false)
    public DrivingSchoolDto patchById(Long id, PatchDrivingSchoolDto patchDrivingSchoolDto) {
        User currentUser = jwtService.currentUser();

        if (currentUser.getRole() != Role.ADMINISTRATOR && currentUser.getRole() != Role.INSTRUCTOR_AND_ADMINISTRATOR) {
            String errorMessage = String.format("User with id %d is not an administrator", currentUser.getId());
            log.warn(errorMessage);
            throw new NoAccessException(errorMessage);
        }

        Optional<DrivingSchool> drivingSchoolOptional = drivingSchoolRepository.findById(id);
        if (drivingSchoolOptional.isEmpty()) {
            String errorMessage = String.format("Driving school with id %d does not exits", id);
            log.warn(errorMessage);
            throw new ObjectNotFoundException(errorMessage);
        }
        DrivingSchool drivingSchool = drivingSchoolOptional.get();

        User user = null;
        for (User admin : drivingSchool.getAdmins()) {
            if (admin.getId().equals(currentUser.getId())) {
                user = admin;
                break;
            }
        }
        if (user == null) {
            String errorMessage = String.format("User with id %d is not an administrator for school with id %d",
                    currentUser.getId(), id);
            log.warn(errorMessage);
            throw new NoAccessException(errorMessage);
        }

        if (patchDrivingSchoolDto.getAddress() != null)
            drivingSchool.setAddress(patchDrivingSchoolDto.getAddress());
        if (patchDrivingSchoolDto.getName() != null)
            drivingSchool.setName(patchDrivingSchoolDto.getName());
        if (patchDrivingSchoolDto.getDescription() != null)
            drivingSchool.setDescription(patchDrivingSchoolDto.getDescription());
        if (patchDrivingSchoolDto.getPhoneNumber() != null)
            drivingSchool.setPhoneNumber(patchDrivingSchoolDto.getPhoneNumber());

        drivingSchool = drivingSchoolRepository.save(drivingSchool);
        log.info("Driving school patched: {}", drivingSchool);

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

    @Transactional(readOnly = false)
    public void deleteById(Long id) {
        User currentUser = jwtService.currentUser();

        if (currentUser.getRole() != Role.ADMINISTRATOR && currentUser.getRole() != Role.INSTRUCTOR_AND_ADMINISTRATOR) {
            String errorMessage = String.format("User with id %d is not an administrator", currentUser.getId());
            log.warn(errorMessage);
            throw new NoAccessException(errorMessage);
        }

        Optional<DrivingSchool> drivingSchoolOptional = drivingSchoolRepository.findById(id);
        if (drivingSchoolOptional.isEmpty()) {
            String errorMessage = String.format("Driving school with id %d does not exits", id);
            log.warn(errorMessage);
            throw new ObjectNotFoundException(errorMessage);
        }
        DrivingSchool drivingSchool = drivingSchoolOptional.get();

        User user = null;
        for (User admin : drivingSchool.getAdmins()) {
            if (admin.getId().equals(currentUser.getId())) {
                user = admin;
                break;
            }
        }
        if (user == null) {
            String errorMessage = String.format("User with id %d is not an administrator for school with id %d",
                    currentUser.getId(), id);
            log.warn(errorMessage);
            throw new NoAccessException(errorMessage);
        }

        drivingSchoolRepository.delete(drivingSchool);
    }

    @Transactional(readOnly = false)
    public void deleteAdditionalServiceById(Long drivingSchoolId, Long additionalServiceId) {
        User currentUser = jwtService.currentUser();

        if (currentUser.getRole() != Role.ADMINISTRATOR && currentUser.getRole() != Role.INSTRUCTOR_AND_ADMINISTRATOR) {
            String errorMessage = String.format("User with id %d is not an administrator", currentUser.getId());
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

        Optional<AdditionalService> additionalServiceOptional = additionalServiceRepository.findByIdAndDrivingSchool(additionalServiceId, drivingSchool);
        if (additionalServiceOptional.isEmpty()) {
            String errorMessage = String.format("Driving school with id %d does not have a service with id {}", drivingSchoolId, additionalServiceId);
            log.warn(errorMessage);
            throw new ObjectNotFoundException(errorMessage);
        }
        AdditionalService additionalService = additionalServiceOptional.get();

        additionalServiceRepository.delete(additionalService);
    }
}
