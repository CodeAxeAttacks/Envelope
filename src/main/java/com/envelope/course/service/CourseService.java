package com.envelope.course.service;

import com.envelope.course.dao.CourseRepository;
import com.envelope.course.dto.RegisterCourseDto;
import com.envelope.course.dto.CourseDto;
import com.envelope.course.model.Course;
import com.envelope.driving_school.dao.DrivingSchoolRepository;
import com.envelope.driving_school.model.DrivingSchool;
import com.envelope.exception.exceptions.InvalidInputException;
import com.envelope.exception.exceptions.NoAccessException;
import com.envelope.exception.exceptions.ObjectAlreadyExistsException;
import com.envelope.security.JwtService;
import com.envelope.user.model.Role;
import com.envelope.user.model.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CourseService {

    private final CourseRepository courseRepository;
    private final DrivingSchoolRepository drivingSchoolRepository;
    private final JwtService jwtService;

    @Transactional(readOnly = false)
    public CourseDto register(RegisterCourseDto registerCourseDto, Long drivingSchoolId) {
        User user = jwtService.currentUser();

        if (drivingSchoolId == null || drivingSchoolId < 0) {
            String errorMessage = "Driving school's id must not be null or negative";
            log.warn(errorMessage);
            throw new InvalidInputException(errorMessage);
        }

        if (user.getRole() != Role.ADMINISTRATOR && user.getRole() != Role.INSTRUCTOR_AND_ADMINISTRATOR) {
            String errorMessage = String.format("User with id %d is not an administrator", user.getId());
            log.warn(errorMessage);
            throw new NoAccessException(errorMessage);
        }

        Optional<DrivingSchool> drivingSchoolOptional = drivingSchoolRepository.findById(drivingSchoolId);
        if (drivingSchoolOptional.isEmpty()) {
            String errorMessage = String.format("Driving school with id %d does not exist", drivingSchoolId);
            log.warn(errorMessage);
            throw new ObjectAlreadyExistsException(errorMessage);
        }
        DrivingSchool drivingSchool = drivingSchoolOptional.get();

        if (!drivingSchool.getAdmins().contains(user)) {
            String errorMessage = String.format("User with id %d is not an administrator of a driving school with id %d", user.getId(), drivingSchool.getId());
            log.warn(errorMessage);
            throw new NoAccessException(errorMessage);
        }

        Course course = courseRepository.save(Course.builder()
                .name(registerCourseDto.getName())
                .price(registerCourseDto.getPrice())
                .duration(registerCourseDto.getDuration())
                .description(registerCourseDto.getDescription())
                .vehicleCategory(registerCourseDto.getVehicleCategory())
                .studyFormat(registerCourseDto.getStudyFormat())
                .drivingSchool(drivingSchool)
                .build());
        log.info("Course registered: {}", course);

        return CourseDto.builder()
                .id(course.getId())
                .name(course.getName())
                .price(course.getPrice())
                .duration(course.getDuration())
                .description(course.getDescription())
                .vehicleCategory(course.getVehicleCategory())
                .studyFormat(course.getStudyFormat())
                .build();
    }

}
