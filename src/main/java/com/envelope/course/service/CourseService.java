package com.envelope.course.service;

import com.envelope.course.dao.CourseRepository;
import com.envelope.course.dto.CourseDto;
import com.envelope.course.dto.CreateCourseDto;
import com.envelope.course.model.Course;
import com.envelope.exception.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public Course createCourse(CreateCourseDto createCourseDto) {
        Course course = Course.builder()
                .name(createCourseDto.getName())
                .price(createCourseDto.getPrice())
                .duration(createCourseDto.getDuration())
                .description(createCourseDto.getDescription())
                .vehicleCategory(createCourseDto.getVehicleCategory())
                .studyFormat(createCourseDto.getStudyFormat())
                .build();
        return courseRepository.save(course);
    }

    public CourseDto convertToDto(Course course) {
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

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Course not found with id " + id));
    }

    @Transactional
    public Course updateCourse(Long id, Course updatedCourse) {
        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Course not found with id " + id));

        Optional.ofNullable(updatedCourse.getName()).ifPresent(existingCourse::setName);
        Optional.ofNullable(updatedCourse.getPrice()).ifPresent(existingCourse::setPrice);
        Optional.ofNullable(updatedCourse.getDuration()).ifPresent(existingCourse::setDuration);
        Optional.ofNullable(updatedCourse.getDescription()).ifPresent(existingCourse::setDescription);
        Optional.ofNullable(updatedCourse.getVehicleCategory()).ifPresent(existingCourse::setVehicleCategory);
        Optional.ofNullable(updatedCourse.getStudyFormat()).ifPresent(existingCourse::setStudyFormat);

        return courseRepository.save(existingCourse);
    }

    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Course not found with id " + id));
        courseRepository.delete(course);
    }

}
