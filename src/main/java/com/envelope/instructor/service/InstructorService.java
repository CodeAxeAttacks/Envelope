package com.envelope.instructor.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.envelope.exception.exceptions.ObjectAlreadyExistsException;
import com.envelope.exception.exceptions.ObjectNotFoundException;
import com.envelope.exception.exceptions.InvalidInputException;
import com.envelope.exception.exceptions.NoAccessException;
import com.envelope.instructor.dao.InstructorRepository;
import com.envelope.instructor.dao.InstructorServiceRepository;
import com.envelope.instructor.dto.instructor.RegisterInstructorDto;
import com.envelope.instructor.dto.instructor_service.AddInstructorServiceDto;
import com.envelope.instructor.dto.instructor_service.InstructorServiceDto;
import com.envelope.instructor.dto.instructor_service.PatchInstructorServiceDto;
import com.envelope.instructor.dto.instructor.InstructorDto;
import com.envelope.instructor.dto.instructor.PatchInstructorDto;
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
public class InstructorService {

    private final InstructorRepository instructorRepository;
    private final InstructorServiceRepository instructorServiceRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public List<InstructorDto> getAll() {
        return instructorRepository.findAll().stream()
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

    public InstructorDto getById(Long instructorId) {
        if (instructorId == null || instructorId < 0) {
            String errorMessage = "Instructor's id must not be null or negative";
            log.warn(errorMessage);
            throw new InvalidInputException(errorMessage);
        }

        Optional<Instructor> instructorOptional = instructorRepository.findById(instructorId);
        if (instructorOptional.isEmpty()) {
            String errorMessage = String.format("Instructor with id %d does not exist", instructorId);
            log.warn(errorMessage);
            throw new ObjectNotFoundException(errorMessage);
        }
        Instructor instructor = instructorOptional.get();
        User user = instructor.getUser();

        return InstructorDto.builder()
                .id(instructor.getId())
                .user(UserDto.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .phone(user.getPhone())
                        .createdAt(user.getCreatedAt())
                        .role(user.getRole())
                        .status(user.getStatus())
                        .instructorId(instructor.getId())
                        .build())
                .experience(instructor.getExperience())
                .description(instructor.getDescription())
                .rating(instructor.getRating())
                .build();
    }

    public List<InstructorServiceDto> getAllInstructorServicesByInstructorId(Long instructorId) {
        if (instructorId == null || instructorId < 0) {
            String errorMessage = "Instructor's id must not be null or negative";
            log.warn(errorMessage);
            throw new InvalidInputException(errorMessage);
        }

        Optional<Instructor> instructorOptional = instructorRepository.findById(instructorId);
        if (instructorOptional.isEmpty()) {
            String errorMessage = String.format("Instructor with id %d does not exist", instructorId);
            log.warn(errorMessage);
            throw new ObjectNotFoundException(errorMessage);
        }
        Instructor instructor = instructorOptional.get();
        List<com.envelope.instructor.model.InstructorService> services = instructorServiceRepository
                .findAllByInstructor(instructor);

        return services.stream()
                .map(service -> InstructorServiceDto.builder()
                        .id(service.getId())
                        .name(service.getName())
                        .description(service.getDescription())
                        .price(service.getPrice())
                        .instructorId(service.getInstructor().getId())
                        .build())
                .toList();
    }

    @Transactional(readOnly = false)
    public InstructorDto register(RegisterInstructorDto registerInstructorDto) {
        User user = jwtService.currentUser();

        if (user.getRole() == Role.INSTRUCTOR || user.getRole() == Role.INSTRUCTOR_AND_ADMINISTRATOR) {
            String errorMessage = String.format("User with id %d is already an instructor", user.getId());
            log.warn(errorMessage);
            throw new ObjectAlreadyExistsException(errorMessage);
        }

        if (registerInstructorDto.getExperience() < 0) {
            String errorMessage = "Field \"experience\" must not be negative";
            log.warn(errorMessage);
            throw new InvalidInputException(errorMessage);
        }

        Instructor instructor = Instructor.builder()
                .user(user)
                .experience(registerInstructorDto.getExperience())
                .description(registerInstructorDto.getDescription())
                .rating(0f)
                .build();

        if (user.getRole() == Role.USER)
            user.setRole(Role.INSTRUCTOR);
        else
            user.setRole(Role.INSTRUCTOR_AND_ADMINISTRATOR);

        instructor = instructorRepository.save(instructor);
        user = userRepository.save(user);
        log.info("Registered instructor: {}", instructor);

        return InstructorDto.builder()
                .id(instructor.getId())
                .user(UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .createdAt(user.getCreatedAt())
                .role(user.getRole())
                .status(user.getStatus())
                .instructorId(instructor.getId())
                .build())
                .experience(instructor.getExperience())
                .description(instructor.getDescription())
                .rating(instructor.getRating())
                .build();
    }

    @Transactional(readOnly = false)
    public InstructorServiceDto addService(AddInstructorServiceDto addInstructorServiceDto) {
        User user = jwtService.currentUser();

        if (user.getRole() != Role.INSTRUCTOR && user.getRole() != Role.INSTRUCTOR_AND_ADMINISTRATOR) {
            String errorMessage = String.format("User with id %d is not an instructor", user.getId());
            log.warn(errorMessage);
            throw new ObjectNotFoundException(errorMessage);
        }

        if (addInstructorServiceDto.getPrice() < 0) {
            String errorMessage = "Field \"price\" must not be negative";
            log.warn(errorMessage);
            throw new InvalidInputException(errorMessage);
        }

        Instructor instructor = instructorRepository.findByUser(user).get();

        com.envelope.instructor.model.InstructorService service = com.envelope.instructor.model.InstructorService
                .builder()
                .name(addInstructorServiceDto.getName())
                .description(addInstructorServiceDto.getDescription())
                .price(addInstructorServiceDto.getPrice())
                .instructor(instructor)
                .build();
        service = instructorServiceRepository.save(service);

        return InstructorServiceDto.builder()
                .id(service.getId())
                .name(service.getName())
                .description(service.getDescription())
                .price(service.getPrice())
                .instructorId(service.getInstructor().getId())
                .build();
    }

    @Transactional(readOnly = false)
    public InstructorDto patchInstructor(PatchInstructorDto patchInstructorDto) {
        User user = jwtService.currentUser();

        if (user.getRole() != Role.INSTRUCTOR && user.getRole() != Role.INSTRUCTOR_AND_ADMINISTRATOR) {
            String errorMessage = String.format("User with id %d is not an instructor", user.getId());
            log.warn(errorMessage);
            throw new ObjectNotFoundException(errorMessage);
        }

        Instructor instructor = instructorRepository.findByUser(user).get();

        if (patchInstructorDto.getDescription() != null)
            instructor.setDescription(patchInstructorDto.getDescription());
        if (patchInstructorDto.getExperience() != null) {
            if (patchInstructorDto.getExperience() < 0) {
                String errorMessage = "Field \"experience\" must not be negative";
                log.warn(errorMessage);
                throw new InvalidInputException(errorMessage);
            }
            instructor.setExperience(patchInstructorDto.getExperience());
        }

        instructor = instructorRepository.save(instructor);
        log.info("Patched instructor profile: {}", instructor);

        return InstructorDto.builder()
                .id(instructor.getId())
                .user(UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .createdAt(user.getCreatedAt())
                .role(user.getRole())
                .status(user.getStatus())
                .instructorId(instructor.getId())
                .build())
                .experience(instructor.getExperience())
                .description(instructor.getDescription())
                .rating(instructor.getRating())
                .build();
    }

    @Transactional(readOnly = false)
    public InstructorServiceDto patchInstructorService(Long serviceId,
            PatchInstructorServiceDto patchInstructorServiceDto) {
        User user = jwtService.currentUser();

        if (user.getRole() != Role.INSTRUCTOR && user.getRole() != Role.INSTRUCTOR_AND_ADMINISTRATOR) {
            String errorMessage = String.format("User with id %d is not an instructor", user.getId());
            log.warn(errorMessage);
            throw new NoAccessException(errorMessage);
        }
        Instructor instructor = instructorRepository.findByUser(user).get();

        Optional<com.envelope.instructor.model.InstructorService> serviceOptional = instructorServiceRepository
                .findById(serviceId);
        if (serviceOptional.isEmpty()) {
            String errorMessage = String.format("Instructor service with id %d does not exist", serviceId);
            log.warn(errorMessage);
            throw new ObjectNotFoundException(errorMessage);
        }
        com.envelope.instructor.model.InstructorService service = serviceOptional.get();

        if (!service.getInstructor().getId().equals(instructor.getId())) {
            String errorMessage = String.format("Instructor with id %d does not have a service with id %d",
                    instructor.getId(), serviceId);
            log.warn(errorMessage);
            throw new NoAccessException(errorMessage);
        }

        if (patchInstructorServiceDto.getName() != null)
            service.setName(patchInstructorServiceDto.getName());
        if (patchInstructorServiceDto.getDescription() != null)
            service.setDescription(patchInstructorServiceDto.getDescription());
        if (patchInstructorServiceDto.getPrice() != null) {
            if (patchInstructorServiceDto.getPrice() < 0) {
                String errorMessage = "Field \"price\" must not be negative";
                log.warn(errorMessage);
                throw new InvalidInputException(errorMessage);
            }
            service.setPrice(patchInstructorServiceDto.getPrice());
        }

        service = instructorServiceRepository.save(service);

        return InstructorServiceDto.builder()
                .id(service.getId())
                .name(service.getName())
                .description(service.getDescription())
                .price(service.getPrice())
                .instructorId(service.getInstructor().getId())
                .build();
    }

    @Transactional(readOnly = false)
    public void deleteService(Long serviceId) {
        User user = jwtService.currentUser();

        if (user.getRole() != Role.INSTRUCTOR && user.getRole() != Role.INSTRUCTOR_AND_ADMINISTRATOR) {
            String errorMessage = String.format("User with id %d is not an instructor", user.getId());
            log.warn(errorMessage);
            throw new ObjectNotFoundException(errorMessage);
        }

        Instructor instructor = instructorRepository.findByUser(user).get();

        Optional<com.envelope.instructor.model.InstructorService> serviceOptional = instructorServiceRepository
                .findById(serviceId);
        if (serviceOptional.isEmpty()) {
            String errorMessage = String.format("Instructor service with id %d does not exits", serviceId);
            log.warn(errorMessage);
            throw new ObjectNotFoundException(errorMessage);
        }
        com.envelope.instructor.model.InstructorService service = serviceOptional.get();

        if (!service.getInstructor().getId().equals(instructor.getId())) {
            String errorMessage = String.format("Instructor with id %d does not have a service with id %d",
                    instructor.getId(), serviceId);
            log.warn(errorMessage);
            throw new NoAccessException(errorMessage);
        }

        instructorServiceRepository.delete(service);
    }

}
