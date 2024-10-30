package com.envelope.instructor.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.envelope.exception.exceptions.ObjectAlreadyExistsException;
import com.envelope.exception.exceptions.ObjectNotFoundException;
import com.envelope.instructor.dao.InstructorRepository;
import com.envelope.instructor.dto.RegisterInstructorDto;
import com.envelope.instructor.dto.ResultInstructorDto;
import com.envelope.instructor.model.Instructor;
import com.envelope.user.dao.UserRepository;
import com.envelope.user.dto.ResultUserDto;
import com.envelope.user.model.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class InstructorService {

    private final UserRepository userRepository;
    private final InstructorRepository instructorRepository;

    public ResultInstructorDto register(RegisterInstructorDto registerInstructorDto) {
        Optional<User> userOptional = userRepository.findById(registerInstructorDto.getUserId());
        if (userOptional.isEmpty()) {
            String errorMessage = String.format("User with id %d does not exist", registerInstructorDto.getUserId());
            log.warn(errorMessage);
            throw new ObjectNotFoundException(errorMessage);
        }
        User user = userOptional.get();
        if (instructorRepository.findByUser(user).isPresent()) {
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

        instructor = instructorRepository.save(instructor);
        log.info("Registered instructor: {}", instructor);

        return ResultInstructorDto.builder()
                .id(instructor.getId())
                .user(ResultUserDto.builder()
                        .id(instructor.getUser().getId())
                        .email(instructor.getUser().getEmail())
                        .firstName(instructor.getUser().getFirstName())
                        .lastName(instructor.getUser().getLastName())
                        .phone(instructor.getUser().getPhone())
                        .createdAt(instructor.getUser().getCreatedAt())
                        // .role(instructor.getUser().getRole())
                        // .status(instructor.getUser().getStatus())
                        .build())
                .experience(instructor.getExperience())
                .description(instructor.getDescription())
                .rating(instructor.getRating())
                .build();
    }

}
