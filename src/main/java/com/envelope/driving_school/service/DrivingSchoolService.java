package com.envelope.driving_school.service;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
import com.envelope.exception.exceptions.InvalidInputException;
import com.envelope.exception.exceptions.NoAccessException;
import com.envelope.exception.exceptions.ObjectAlreadyExistsException;
import com.envelope.exception.exceptions.ObjectNotFoundException;
import com.envelope.instructor.dao.InstructorRepository;
import com.envelope.instructor.dto.instructor.InstructorDto;
import com.envelope.instructor.model.Instructor;
import com.envelope.security.JwtService;
import com.envelope.user.dao.UserRepository;
import com.envelope.user.dto.UserDto;
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
    private final InstructorRepository instructorRepository;
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
    public ResponseEntity<Resource> getImage(Long id) {
        if (id == null || id < 0) {
            String errorMessage = "Drivign school's id must not be null or negative";
            log.warn(errorMessage);
            throw new InvalidInputException(errorMessage);
        }

        if (drivingSchoolRepository.findById(id).isEmpty()) {
            String errorMessage = String.format("Driving school with id %d does not exist", id);
            log.warn(errorMessage);
            throw new ObjectNotFoundException(errorMessage);
        }

        File dir = new File("./assets/driving-school");
        if (!dir.exists()) {
            String errorMessage = String.format("Driving school with id %d have not set image yet", id);
            log.warn(errorMessage);
            throw new ObjectNotFoundException(errorMessage);
        }

        File file = new File(dir.getAbsolutePath() + File.separator + id + ".jpeg");
        if (!file.exists()) {
            String errorMessage = String.format("Driving school with id %d have not set image yet", id);
            log.warn(errorMessage);
            throw new ObjectNotFoundException(errorMessage);
        }

        Resource resource = new FileSystemResource(file);

        return ResponseEntity.ok().body(resource);
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

    @Transactional(readOnly = true)
    public List<InstructorDto> getAllInstructorsById(Long id) {
        if (id == null || id < 0) {
            String errorMessage = "Drivign school's id must not be null or negative";
            log.warn(errorMessage);
            throw new InvalidInputException(errorMessage);
        }

        Optional<DrivingSchool> drivingSchoolOptional = drivingSchoolRepository.findById(id);
        if (drivingSchoolOptional.isEmpty()) {
            String errorMessage = String.format("Driving school with id %d does not exist", id);
            log.warn(errorMessage);
            throw new ObjectNotFoundException(errorMessage);
        }
        DrivingSchool drivingSchool = drivingSchoolOptional.get();

        return drivingSchool.getInstructors().stream()
                .map(instructor -> InstructorDto.builder()
                        .id(instructor.getId())
                        .user(UserDto.builder()
                                .id(instructor.getUser().getId())
                                .email(instructor.getUser().getEmail())
                                .firstName(instructor.getUser().getFirstName())
                                .lastName(instructor.getUser().getLastName())
                                .phone(instructor.getUser().getPhone())
                                .createdAt(instructor.getUser().getCreatedAt())
                                .role(instructor.getUser().getRole())
                                .status(instructor.getUser().getStatus())
                                .instructorId(instructor.getUser().getId())
                                .build())
                        .experience(instructor.getExperience())
                        .description(instructor.getDescription())
                        .rating(instructor.getRating())
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

        User user = null;
        for (User admin : drivingSchool.getAdmins()) {
            if (admin.getId().equals(currentUser.getId())) {
                user = admin;
                break;
            }
        }
        if (user == null) {
            String errorMessage = String.format("User with id %d is not an administrator for school with id %d",
                    currentUser.getId(), drivingSchool.getId());
            log.warn(errorMessage);
            throw new NoAccessException(errorMessage);
        }

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

        User user = null;
        for (User admin : drivingSchool.getAdmins()) {
            if (admin.getId().equals(currentUser.getId())) {
                user = admin;
                break;
            }
        }
        if (user == null) {
            String errorMessage = String.format("User with id %d is not an administrator for school with id %d",
                    currentUser.getId(), drivingSchool.getId());
            log.warn(errorMessage);
            throw new NoAccessException(errorMessage);
        }

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
    public InstructorDto addInstructorByEmail(Long id, String email) {
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

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            String errorMessage = String.format("User with email %s does not exist", email);
            log.warn(errorMessage);
            throw new ObjectNotFoundException(errorMessage);
        }
        User userInstructor = userOptional.get();

        if (userInstructor.getRole() != Role.INSTRUCTOR
                && userInstructor.getRole() != Role.INSTRUCTOR_AND_ADMINISTRATOR) {
            String errorMessage = String.format("User with id %d is not an instructor", userInstructor.getId());
            log.warn(errorMessage);
            throw new ObjectNotFoundException(errorMessage);
        }

        Instructor instructor = instructorRepository.findByUser(userInstructor).get();

        drivingSchool.getInstructors().add(instructor);
        drivingSchoolRepository.save(drivingSchool);

        return InstructorDto.builder()
                .id(instructor.getId())
                .user(UserDto.builder()
                        .id(userInstructor.getId())
                        .email(userInstructor.getEmail())
                        .firstName(userInstructor.getFirstName())
                        .lastName(userInstructor.getLastName())
                        .phone(userInstructor.getPhone())
                        .createdAt(userInstructor.getCreatedAt())
                        .role(userInstructor.getRole())
                        .status(userInstructor.getStatus())
                        .instructorId(userInstructor.getId())
                        .build())
                .experience(instructor.getExperience())
                .description(instructor.getDescription())
                .rating(instructor.getRating())
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
    public void setImage(MultipartFile file, Long id) {
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

        String[] filenameAndType = file.getOriginalFilename().split("\\.");
        if (filenameAndType.length != 2 || !filenameAndType[1].equals("jpeg"))
            throw new InvalidInputException("Invalid file type (expected .jpeg)");

        if (file.isEmpty()) {
            String errorMessage = String.format("File must not be empty");
            log.warn(errorMessage);
            throw new InvalidInputException(errorMessage);
        }

        try {
            File dir = new File("./assets/driving-school");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File serverFile = new File(dir.getAbsolutePath() + File.separator + drivingSchool.getId() + ".jpeg");
            file.transferTo(serverFile);
        } catch (IOException e) {
            throw new RuntimeException("Error during saving file");
        }

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

        User user = null;
        for (User admin : drivingSchool.getAdmins()) {
            if (admin.getId().equals(currentUser.getId())) {
                user = admin;
                break;
            }
        }
        if (user == null) {
            String errorMessage = String.format("User with id %d is not an administrator for school with id %d",
                    currentUser.getId(), drivingSchoolId);
            log.warn(errorMessage);
            throw new NoAccessException(errorMessage);
        }

        Optional<AdditionalService> additionalServiceOptional = additionalServiceRepository
                .findByIdAndDrivingSchool(additionalServiceId, drivingSchool);
        if (additionalServiceOptional.isEmpty()) {
            String errorMessage = String.format("Driving school with id %d does not have a service with id {}",
                    drivingSchoolId, additionalServiceId);
            log.warn(errorMessage);
            throw new ObjectNotFoundException(errorMessage);
        }
        AdditionalService additionalService = additionalServiceOptional.get();

        additionalServiceRepository.delete(additionalService);
    }

    @Transactional(readOnly = false)
    public void deleteInstructorById(Long drivingSchoolId, Long instructorId) {

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

        User user = null;
        for (User admin : drivingSchool.getAdmins()) {
            if (admin.getId().equals(currentUser.getId())) {
                user = admin;
                break;
            }
        }
        if (user == null) {
            String errorMessage = String.format("User with id %d is not an administrator for school with id %d",
                    currentUser.getId(), drivingSchoolId);
            log.warn(errorMessage);
            throw new NoAccessException(errorMessage);
        }

        drivingSchool.getInstructors().removeIf(instructor -> instructor.getId().equals(instructorId));
        drivingSchoolRepository.save(drivingSchool);
    }

}
