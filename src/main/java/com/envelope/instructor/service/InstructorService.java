package com.envelope.instructor.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

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

import io.jsonwebtoken.lang.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class InstructorService {

    private final InstructorRepository instructorRepository;
    private final InstructorServiceRepository instructorServiceRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public List<InstructorDto> getAll() {
        return instructorRepository.findAll().stream()
                .map(instructor -> InstructorDto.builder()
                        .id(instructor.getId())
                        .userId(instructor.getUser().getId())
                        .experience(instructor.getExperience())
                        .description(instructor.getDescription())
                        .rating(instructor.getRating())
                        .build())
                .toList();
    }

    public InstructorDto getById(Long instructorId) {
        if (instructorId == null || instructorId < 0) {
            String errorMessage = "User id must not be null or negative";
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

        return InstructorDto.builder()
                .id(instructor.getId())
                .userId(instructor.getUser().getId())
                .experience(instructor.getExperience())
                .description(instructor.getDescription())
                .rating(instructor.getRating())
                .build();
    }

    public InstructorDto register(RegisterInstructorDto registerInstructorDto) {
        System.out.println(0);
        User user = jwtService.currentUser();

        if (user.getRole() == Role.INSTRUCTOR || user.getRole() == Role.INSTRUCTOR_AND_ADMINISTRATOR) {
            String errorMessage = String.format("User with id %d is already an instructor", user.getId());
            log.warn(errorMessage);
            throw new ObjectAlreadyExistsException(errorMessage);
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

        System.out.println(1);
        instructor = instructorRepository.save(instructor);
        System.out.println(2);
        user = userRepository.save(user);
        log.info("Registered instructor: {}", instructor);

        return InstructorDto.builder()
                .id(instructor.getId())
                .userId(user.getId())
                .experience(instructor.getExperience())
                .description(instructor.getDescription())
                .rating(instructor.getRating())
                .build();
    }

    public InstructorServiceDto addService(AddInstructorServiceDto addInstructorServiceDto) {
        User user = jwtService.currentUser();

        if (user.getRole() != Role.INSTRUCTOR && user.getRole() != Role.INSTRUCTOR_AND_ADMINISTRATOR) {
            String errorMessage = String.format("User with id %d is not an instructor", user.getId());
            log.warn(errorMessage);
            throw new ObjectNotFoundException(errorMessage);
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

    public InstructorDto patchInstructor(PatchInstructorDto patchInstructorDto) {
        User user = jwtService.currentUser();

        if (user.getRole() != Role.INSTRUCTOR && user.getRole() != Role.INSTRUCTOR_AND_ADMINISTRATOR) {
            String errorMessage = "User is not an instructor";
            log.warn(errorMessage);
            throw new ObjectNotFoundException(errorMessage);
        }

        Instructor instructor = instructorRepository.findByUser(user).get();

        if (patchInstructorDto.getDescription() != null)
            instructor.setDescription(patchInstructorDto.getDescription());
        if (patchInstructorDto.getExperience() != null)
            instructor.setExperience(patchInstructorDto.getExperience());
        if (patchInstructorDto.getRating() != null)
            instructor.setRating(patchInstructorDto.getRating());

        instructor = instructorRepository.save(instructor);
        log.info("Patched instructor profile: {}", instructor);

        return InstructorDto.builder()
                .id(instructor.getId())
                .userId(instructor.getUser().getId())
                .experience(instructor.getExperience())
                .description(instructor.getDescription())
                .rating(instructor.getRating())
                .build();
    }

    public InstructorServiceDto patchInstructorService(Long serviceId,
            PatchInstructorServiceDto patchInstructorServiceDto) {
        
        

       
        return null;
    }

}
