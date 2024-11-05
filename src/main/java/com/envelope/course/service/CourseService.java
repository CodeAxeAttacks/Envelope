package com.envelope.course.service;

import com.envelope.course.dao.CourseRepository;
import com.envelope.course.dto.RegisterCourseDto;
import com.envelope.course.dto.ResultCourseDto;
import com.envelope.course.model.Course;
import com.envelope.driving_school.dao.DrivingSchoolRepository;
import com.envelope.exception.exceptions.ObjectAlreadyExistsException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseService {

    private final CourseRepository courseRepository;
    private final DrivingSchoolRepository drivingSchoolRepository;

    public ResultCourseDto register(RegisterCourseDto registerCourseDto) {
        if (drivingSchoolRepository.findById(registerCourseDto.getDrivingSchoolId()).isEmpty()) {
            String errorMessage = String.format("Driving school with id %d does not exist",
                    registerCourseDto.getDrivingSchoolId());
            log.warn(errorMessage);
            throw new ObjectAlreadyExistsException(errorMessage);
        }

        Course course = courseRepository.save(Course.builder()
                .name(registerCourseDto.getName())
                .price(registerCourseDto.getPrice())
                .duration(registerCourseDto.getDuration())
                .description(registerCourseDto.getDescription())
                .vehicleCategory(registerCourseDto.getVehicleCategory())
                .studyFormat(registerCourseDto.getStudyFormat())
                .build());

        log.info("Course registered: {}", course);

        return ResultCourseDto.builder()
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
